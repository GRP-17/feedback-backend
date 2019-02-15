package com.group17.feedback.day;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Defines the service that will handle sending the text to a IBM ToneAnalyser for analysing
 */
@Service
public class DayService {
	@Autowired private DayRepository day;
	@Autowired private DayResourceAssembler assembler;
	
}