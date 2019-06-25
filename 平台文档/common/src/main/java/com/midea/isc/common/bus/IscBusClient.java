package com.midea.isc.common.bus;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface IscBusClient {
    String GROUP_INPUT = "groupInput";
    String GROUP_OUTPUT = "groupOutput";
    String GROUP_DESTINATION= "groupDes";
    String BROADCAST_INPUT = "broadcastInput";
    String BROADCAST_OUTPUT = "broadcastOutput";
    String BROADCAST_DESTINATION = "broadcastDes";
    String DEFAULT_OUTPUT = "springCloudBusOutput";
    String DEFAULT_INPUT = "springCloudBusInput";

    @Output(BROADCAST_OUTPUT)
    MessageChannel broadcastOutput();

    @Output(GROUP_OUTPUT)
    MessageChannel groupOutput();

    @Input(DEFAULT_INPUT)
    SubscribableChannel defaultInput();

    @Input(BROADCAST_INPUT)
    SubscribableChannel broadcastInput();

    @Output(DEFAULT_OUTPUT)
    MessageChannel defaultOutput();

    @Input(GROUP_INPUT)
    SubscribableChannel groupInput();
}
