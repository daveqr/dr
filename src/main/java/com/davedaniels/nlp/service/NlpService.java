package com.davedaniels.nlp.service;

import com.davedaniels.nlp.model.NlpData;


public interface NlpService {

   /**
    * Process {@link #sourceFileName} and writes the XML representation to {@link System#out}.
    * 
    * @throws Exception
    */
   NlpData process() throws Exception;

}