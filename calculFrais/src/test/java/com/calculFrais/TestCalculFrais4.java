package com.calculFrais;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;


import com.calculFrais.*;

@RunWith(Parameterized.class)
public class TestCalculFrais4 {
	
	@Parameters public static Collection<Object[]> val(){
		return Arrays.asList(new Object [][] {{100.0,30.0},{300.0,20.0},{500.0,0.0}});
	}
	private double total;
	private double montant;
	
	public TestCalculFrais4 (double total, double montant)
	{this.total=total;
	this.montant=montant;
	}
	
	@Test
	public void test() {
		RecupLimites rec = new RecupLimites();
		CalculFrais frais = new CalculFrais(rec);
		assertEquals(montant,frais.montant(total),0.01);
	}
}
