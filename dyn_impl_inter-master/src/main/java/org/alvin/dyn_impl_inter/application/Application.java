package org.alvin.dyn_impl_inter.application;

import org.alvin.mini_inject.InjectContext;
import org.alvin.mini_inject.annotations.MiniServiceScan;

@MiniServiceScan("org.alvin.dyn_impl_inter")
public class Application {

	public static void main(String [] args){
		InjectContext.run(Application.class, args);
	}
}
