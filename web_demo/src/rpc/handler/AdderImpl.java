package rpc.handler;

import po.Ad;

public class AdderImpl implements Adder {
	public int add(int pNum1, int pNum2) {
		return pNum1 + pNum2;
	}

	public String add2(String s, Ad ad) {
		return s + ad.getId();
	}

}