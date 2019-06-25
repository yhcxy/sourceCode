package com.midea.isc.common.config;

import com.midea.isc.common.bus.IscBusClient;
import com.midea.isc.common.bus.IscServiceMatcher;
import com.midea.isc.common.event.IscBroadcastEvent;
import com.midea.isc.common.event.IscGroupEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnEnabledEndpoint;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.bus.BusPathMatcher;
import org.springframework.cloud.bus.BusProperties;
import org.springframework.cloud.bus.ConditionalOnBusEnabled;
import org.springframework.cloud.bus.DefaultBusPathMatcher;
import org.springframework.cloud.bus.endpoint.EnvironmentBusEndpoint;
import org.springframework.cloud.bus.endpoint.RefreshBusEndpoint;
import org.springframework.cloud.bus.event.*;
import org.springframework.cloud.context.environment.EnvironmentManager;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.config.BindingProperties;
import org.springframework.cloud.stream.config.BindingServiceProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.annotation.PostConstruct;

@Configuration
@Order(2)
@ConditionalOnBusEnabled
@EnableBinding({IscBusClient.class})
@EnableConfigurationProperties({BusProperties.class})
public class BusConfig implements ApplicationEventPublisherAware {
    @Autowired
    @Output(IscBusClient.GROUP_OUTPUT)
    private MessageChannel groupOutputChannel;
    @Autowired
    @Output(IscBusClient.BROADCAST_OUTPUT)
    private MessageChannel broadcastOutputChannel;
    @Autowired
    @Output(IscBusClient.DEFAULT_OUTPUT)
    private MessageChannel defaultOutputChannel;
    @Autowired
    private IscServiceMatcher serviceMatcher;
    @Autowired
    private BindingServiceProperties bindings;
    @Autowired
    private BusProperties bus;
    private ApplicationEventPublisher applicationEventPublisher;

    @Value("${spring.cloud.bus.doubleChannel:false}")
    private boolean doubleChannel;

    public BusConfig() {
    }

    @PostConstruct
    public void init() {
        BindingProperties input = (BindingProperties)this.bindings.getBindings().get(IscBusClient.BROADCAST_INPUT);
        if (input.getDestination() == null) {
            input.setDestination(IscBusClient.BROADCAST_DESTINATION);
        }

        input = (BindingProperties)this.bindings.getBindings().get(IscBusClient.GROUP_INPUT);
        if (input.getDestination() == null) {
            input.setDestination(IscBusClient.GROUP_DESTINATION);
        }

        input = (BindingProperties)this.bindings.getBindings().get(IscBusClient.DEFAULT_INPUT);
        if (input.getDestination() == null) {
            input.setDestination(this.bus.getDestination());
        }

        BindingProperties output = (BindingProperties)this.bindings.getBindings().get(IscBusClient.GROUP_OUTPUT);
        if (output.getDestination() == null) {
            output.setDestination(IscBusClient.GROUP_DESTINATION);
        }

        output = (BindingProperties)this.bindings.getBindings().get(IscBusClient.BROADCAST_OUTPUT);
        if (output.getDestination() == null) {
            output.setDestination(IscBusClient.BROADCAST_DESTINATION);
        }

        output = (BindingProperties)this.bindings.getBindings().get(IscBusClient.DEFAULT_OUTPUT);
        if (output.getDestination() == null) {
            output.setDestination(this.bus.getDestination());
        }
    }

    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @EventListener(classes = {RemoteApplicationEvent.class})
    public void acceptLocal(RemoteApplicationEvent event) {
        if (this.serviceMatcher.isFromSelf(event) && !(event instanceof AckRemoteApplicationEvent)) {
            if(!this.doubleChannel){
                this.defaultOutputChannel.send(MessageBuilder.withPayload(event).build());
            }else if(event instanceof IscGroupEvent)
                this.groupOutputChannel.send(MessageBuilder.withPayload(event).build());
            else if(event instanceof IscBroadcastEvent){
                this.broadcastOutputChannel.send(MessageBuilder.withPayload(event).build());
            }
        }
    }

    @StreamListener(IscBusClient.GROUP_INPUT)
    public void acceptGroupRemote(RemoteApplicationEvent event) {
        this.input(event);
    }

    @StreamListener(IscBusClient.BROADCAST_INPUT)
    public void acceptBroadcastRemote(RemoteApplicationEvent event) {
        this.input(event);
    }

    @StreamListener(IscBusClient.DEFAULT_INPUT)
    public void acceptRemote(RemoteApplicationEvent event) {
        if (event instanceof AckRemoteApplicationEvent) {
            if (this.bus.getTrace().isEnabled() && !this.serviceMatcher.isFromSelf(event) && this.applicationEventPublisher != null) {
                this.applicationEventPublisher.publishEvent(event);
            }
        } else {
            if (this.serviceMatcher.isForSelf(event) && this.applicationEventPublisher != null) {
                if (!this.serviceMatcher.isFromSelf(event)) {
                    this.applicationEventPublisher.publishEvent(event);
                }

                if (this.bus.getAck().isEnabled()) {
                    AckRemoteApplicationEvent ack = new AckRemoteApplicationEvent(this, this.serviceMatcher.getServiceId(), this.bus.getAck().getDestinationService(), event.getDestinationService(), event.getId(), event.getClass());
                    this.defaultOutputChannel.send(MessageBuilder.withPayload(ack).build());
                    this.applicationEventPublisher.publishEvent(ack);
                }
            }

            if (this.bus.getTrace().isEnabled() && this.applicationEventPublisher != null) {
                this.applicationEventPublisher.publishEvent(new SentApplicationEvent(this, event.getOriginService(), event.getDestinationService(), event.getId(), event.getClass()));
            }
        }
    }

    @Bean
    @ConditionalOnProperty(
            value = {"spring.cloud.bus.refresh.enabled"},
            matchIfMissing = true
    )
    @ConditionalOnBean({ContextRefresher.class})
    public RefreshListener refreshListener(ContextRefresher contextRefresher) {
        return new RefreshListener(contextRefresher);
    }

    @Configuration
    @ConditionalOnClass({EnvironmentManager.class})
    @ConditionalOnBean({EnvironmentManager.class})
    protected static class BusEnvironmentConfiguration {
        protected BusEnvironmentConfiguration() {
        }

        @Bean
        @ConditionalOnProperty(
                value = {"spring.cloud.bus.env.enabled"},
                matchIfMissing = true
        )
        public EnvironmentChangeListener environmentChangeListener() {
            return new EnvironmentChangeListener();
        }

        @Configuration
        @ConditionalOnClass({Endpoint.class})
        protected static class EnvironmentBusEndpointConfiguration {
            protected EnvironmentBusEndpointConfiguration() {
            }

            @Bean
            @ConditionalOnEnabledEndpoint
            public EnvironmentBusEndpoint environmentBusEndpoint(ApplicationContext context, BusProperties bus) {
                return new EnvironmentBusEndpoint(context, bus.getId());
            }
        }
    }

    @Configuration
    @ConditionalOnClass({Endpoint.class})
    @ConditionalOnBean({HttpTraceRepository.class})
    @ConditionalOnProperty(
            value = {"spring.cloud.bus.trace.enabled"},
            matchIfMissing = false
    )
    protected static class BusAckTraceConfiguration {
        protected BusAckTraceConfiguration() {
        }

        @Bean
        @ConditionalOnMissingBean
        public TraceListener ackTraceListener(HttpTraceRepository repository) {
            return new TraceListener(repository);
        }
    }

    @Configuration
    @ConditionalOnClass({Endpoint.class, RefreshScope.class})
    protected static class BusRefreshConfiguration {
        protected BusRefreshConfiguration() {
        }

        @Configuration
        @ConditionalOnBean({ContextRefresher.class})
        @ConditionalOnProperty(
                value = {"endpoints.spring.cloud.bus.refresh.enabled"},
                matchIfMissing = true
        )
        protected static class BusRefreshEndpointConfiguration {
            protected BusRefreshEndpointConfiguration() {
            }

            @Bean
            public RefreshBusEndpoint refreshBusEndpoint(ApplicationContext context, BusProperties bus) {
                return new RefreshBusEndpoint(context, bus.getId());
            }
        }
    }

    @Configuration
    @Order(1)
    protected static class MatcherConfiguration {
        protected MatcherConfiguration() {
        }

        @BusPathMatcher
        @ConditionalOnMissingBean(name = {"busPathMatcher"})
        @Bean(name = {"busPathMatcher"})
        public PathMatcher busPathMatcher() {
            return new DefaultBusPathMatcher(new AntPathMatcher(":"));
        }

        @Bean
        public IscServiceMatcher serviceMatcher(@BusPathMatcher PathMatcher pathMatcher, BusProperties properties) {
            IscServiceMatcher serviceMatcher = new IscServiceMatcher(pathMatcher, properties.getId());
            return serviceMatcher;
        }
    }

    private void input(RemoteApplicationEvent event){
        if (event instanceof AckRemoteApplicationEvent) {
            if (this.bus.getTrace().isEnabled() && !this.serviceMatcher.isFromSelf(event) && this.applicationEventPublisher != null) {
                this.applicationEventPublisher.publishEvent(event);
            }

        } else {
            if (this.serviceMatcher.isForSelf(event) && this.applicationEventPublisher != null) {
                if (!this.serviceMatcher.isFromSelf(event)) {
                    this.applicationEventPublisher.publishEvent(event);
                }

                if (this.bus.getAck().isEnabled()) {
                    AckRemoteApplicationEvent ack = new AckRemoteApplicationEvent(this, this.serviceMatcher.getServiceId(), this.bus.getAck().getDestinationService(), event.getDestinationService(), event.getId(), event.getClass());

                    if(this.doubleChannel){
                        if(event instanceof IscGroupEvent && !this.serviceMatcher.isFromSelfGroup(event))
                            this.groupOutputChannel.send(MessageBuilder.withPayload(ack).build());
                        else if(event instanceof IscBroadcastEvent && !this.serviceMatcher.isFromSelfGroup(event)){
                            this.broadcastOutputChannel.send(MessageBuilder.withPayload(ack).build());
                        }
                    }else{
                        this.defaultOutputChannel.send(MessageBuilder.withPayload(ack).build());
                    }

                    this.applicationEventPublisher.publishEvent(ack);
                }
            }

            if (this.bus.getTrace().isEnabled() && this.applicationEventPublisher != null) {
                this.applicationEventPublisher.publishEvent(new SentApplicationEvent(this, event.getOriginService(), event.getDestinationService(), event.getId(), event.getClass()));
            }
        }
    }
}
