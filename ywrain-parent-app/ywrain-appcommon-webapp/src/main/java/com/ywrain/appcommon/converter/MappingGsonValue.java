/*
 * Copyright 2002-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.ywrain.appcommon.converter;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * A simple holder for the POJO to serialize via {@link MappingJackson2HttpMessageConverter} along
 * with further serialization instructions to be passed in to the converter.
 *
 * <p>
 * On the server side this wrapper is added with a {@code ResponseBodyInterceptor} after content
 * negotiation selects the converter to use but before the write.
 *
 * <p>
 * On the client side, simply wrap the POJO and pass it in to the {@code RestTemplate}.
 *
 * @author Rossen Stoyanchev
 * @since 4.1
 */
public class MappingGsonValue {

    private Object value;

    private String jsonpFunction;


    /**
     * Create a new instance wrapping the given POJO to be serialized.
     * 
     * @param value the Object to be serialized
     */
    public MappingGsonValue(Object value) {
        this.value = value;
    }


    /**
     * Modify the POJO to serialize.
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Return the POJO that needs to be serialized.
     */
    public Object getValue() {
        return this.value;
    }

    /**
     * Set the name of the JSONP function name.
     */
    public void setJsonpFunction(String functionName) {
        this.jsonpFunction = functionName;
    }

    /**
     * Return the configured JSONP function name.
     */
    public String getJsonpFunction() {
        return this.jsonpFunction;
    }

}
