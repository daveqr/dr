/* Copyright (c) 2016 Dave Daniels */
package com.davedaniels.nlp.service;

import com.davedaniels.nlp.model.NlpData;

/**
 * Text processing service.
 * 
 * @since 1.0.0
 */
public interface NlpService {

   /**
    * Process the source strings to a model object.
    * 
    * @throws Exception
    */
   NlpData process() throws Exception;

}