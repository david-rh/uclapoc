package com.redat.poc.ucla.transforms;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.redat.poc.ucla.response.PatientWSResponseA;
import com.redhat.poc.ucla.Patient;

public class PatientWSResponseATransform implements Processor {

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
		PatientWSResponseA wsResponse = new PatientWSResponseA();
		wsResponse.setName(patientRecord.getName());
		wsResponse.setDob(patientRecord.getDob());
		wsResponse.setGender(patientRecord.getGender());
		String ssn = patientRecord.getSsn();
		ssn = ssn.substring(ssn.length()-4);
		wsResponse.setSsn(ssn);
		wsResponse.setZipcode(patientRecord.getZip());
		
		exchange.getOut().setBody(wsResponse);

	}

}
