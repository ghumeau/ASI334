package com.calculFrais;

public class CalculFrais  {
	
	private double limit1;
	private double limit2;
	
	public CalculFrais(RecupLimites param)
	{
		limit1 = param.getLimit1();
		limit2 = param.getLimit2();

	}
	public double montant(double total)
	{
		if (total >= limit2)
			return 0.0;
		else if (total >= limit1)
			return 20.0;
		else 
			return 30.0;
	}
	
	
	
}
