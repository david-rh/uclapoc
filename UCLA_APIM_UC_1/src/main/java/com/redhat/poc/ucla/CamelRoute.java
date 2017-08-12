package com.redhat.poc.ucla;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.CxfEndpoint;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;

import com.redat.poc.ucla.transforms.PatientFHIResponseTransform;
import com.redat.poc.ucla.transforms.PatientRESTResponseTransform;

public class CamelRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		restConfiguration("spark-rest").port(8880).bindingMode(RestBindingMode.auto);
		
		// REST Service Client D example
		rest("/uclarest")
		     .get("/patient/{id}").to("direct:restapi");
		
		// REST Service Client C example
		rest("/uclafhir")
		     .get("/patient/{id}").to("direct:fhirapi");
		  
		  from("direct:restapi").to("direct:ucclienta")
			.process(new PatientRESTResponseTransform())
			.marshal().json(JsonLibrary.Gson)
			.log(LoggingLevel.DEBUG,"********\n${body}\n").end();
		
		  from("direct:fhirapi").to("direct:ucclienta")
			.process(new PatientFHIResponseTransform())
//			.marshal().json(JsonLibrary.Gson)
			.marshal().jaxb()
			.log(LoggingLevel.DEBUG,"********\n${body}\n").end();

		  // SOAP Service Client A example
//		from(websvc)
//		   .log("${body}");
		  
		from("direct:ucclienta")
		.log(LoggingLevel.DEBUG,"******* ${header.camelHttpPath}")
		.setHeader(Exchange.HTTP_PATH).simple("/webapi/allPatients/${header.id}")
		.toD("http://1-dot-patientdemo-175606.appspot.com?"
				+ "bridgeEndpoint=true&authMethod=Basic&httpClient.authenticationPreemptive=true&authUsername=apim&authPassword=apimdemo")
		.unmarshal().jaxb("com.redhat.poc.ucla")
		.log(LoggingLevel.DEBUG,"********\n${body}\n")
		;
	}

}
