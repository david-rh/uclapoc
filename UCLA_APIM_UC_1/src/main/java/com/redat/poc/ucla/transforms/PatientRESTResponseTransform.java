package com.redat.poc.ucla.transforms;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.redat.poc.ucla.response.PatientRESTResponse;
import com.redhat.poc.ucla.Patient;

public class PatientRESTResponseTransform implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		// Get message body
		Patient patientRecord = exchange.getIn().getBody(Patient.class);
		
		// check that record is not null
		if(patientRecord == null){
			exchange.getIn().setBody("No Patient record found.");
			return;
		}
		
		// Map Patient record to REST response
		PatientRESTResponse restResponse = new PatientRESTResponse();
		restResponse.setName(patientRecord.getName());
		restResponse.setDob(patientRecord.getDob());
		
		exchange.getOut().setBody(restResponse);

	}

}
