package org.alvin.dyn_impl_inter.test;

import org.alvin.dyn_impl_inter.annotations.DynImpl;

import java.util.List;

@DynImpl
public interface TestInterFace {

	List<String> getList();

	String getName();

	void test();
}
