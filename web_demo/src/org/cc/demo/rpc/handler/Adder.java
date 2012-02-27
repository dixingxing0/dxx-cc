package org.cc.demo.rpc.handler;

import org.cc.demo.po.Ad;

public interface Adder {
	public int add(int pNum1, int pNum2);

	public String add2(String s, Ad ad);
}