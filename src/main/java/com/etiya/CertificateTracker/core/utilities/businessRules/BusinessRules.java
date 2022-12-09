package com.etiya.CertificateTracker.core.utilities.businessRules;


import com.etiya.CertificateTracker.core.utilities.results.Result;

public class BusinessRules {
	public static Result run(Result ...logics) {
		for(Result logic : logics) {
			if(!logic.isSuccess()) {
				return logic;
			}
		}
		return null;
	}
}
