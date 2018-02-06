package com.calculFrais;

import static org.junit.Assert.*;

import org.easymock.*;
import org.junit.Before;
import org.junit.Test;


import com.calculFrais.*;

public class TestAvecMock {
	
	private RecupLimites mock=null;

	@Before
	public void setUp(){
		 mock = EasyMock.createNiceMock(RecupLimites.class);
	}
	

	@Test
	public void test() {


	}

}
