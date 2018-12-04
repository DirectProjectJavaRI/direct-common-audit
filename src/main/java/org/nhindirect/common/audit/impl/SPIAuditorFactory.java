/* 
Copyright (c) 2010, NHIN Direct Project
All rights reserved.
Authors:
   Greg Meyer      gm2552@cerner.com
 
Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer 
in the documentation and/or other materials provided with the distribution.  Neither the name of the The NHIN Direct Project (nhindirect.org). 
nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS 
BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE 
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
THE POSSIBILITY OF SUCH DAMAGE.
*/

package org.nhindirect.common.audit.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Vector;

import org.nhindirect.common.audit.Auditor;

/**
 * Factory for creating {@link Auditor} instances using Java SPI.  If multiple service provider implementation are located, an instance of
 * each auditor implementation is created and placed in an {@link MultiProviderAuditor} instance.  
 * @author Greg Meyer
 * @since 1.0
 */
public class SPIAuditorFactory
{
	private static Map<ClassLoader,SPIAuditorFactory> INSTANCE_MAP = new HashMap<>();
	
	private final Auditor implementation;
	
	/**
	 * Gets an instance of the SPIAuditorFactory.
	 * @param cl
	 * @return
	 */
	public synchronized static SPIAuditorFactory getInstance(ClassLoader cl)
	{
		final ClassLoader lookupLoader = (cl != null) ? cl : SPIAuditorFactory.class.getClassLoader();
		
		SPIAuditorFactory INSTANCE = INSTANCE_MAP.get(lookupLoader);
		
		if (INSTANCE == null)
		{
			INSTANCE = new SPIAuditorFactory(lookupLoader);
			INSTANCE_MAP.put(lookupLoader, INSTANCE);
		}
		
		return INSTANCE;
	}
	
	/**
	 * Default constructor.  Searches for available implementations of the {@link Auditor} interface using Java SPI (service provider interface)
	 * using the provided class loader to search for implementations.  If the class loader is null, the Auditor class's loader will be used.
	 * @param cLoader Class loader used to lookup services.
	 */
	private SPIAuditorFactory(ClassLoader cLoader)
	{
		final ServiceLoader<Auditor> loader = ServiceLoader.load(Auditor.class, cLoader);
		
		Vector<Auditor> implementations;
		
		Iterator<Auditor> iter = null;
		
		if (loader != null && (iter = loader.iterator()) != null) 
		{
			implementations = new Vector<Auditor>();
			while (iter.hasNext())
				implementations.add(iter.next());
			
			if (implementations.size() == 0)
				implementation = null;
			else if (implementations.size() == 1)
				implementation = implementations.firstElement();
			else
				implementation = new MultiProviderAuditor(implementations);
				
		}
		else
			implementation = null;
	}
	
	/**
	 * Indicates if a service provider implementation is available.
	 * @return True is a service provider implementation is available.  False otherwise.
	 */
	public boolean isImplementationAvailable()
	{
		return implementation != null;
	}	
	
	public Auditor getAuditorImplementation() 
	{
		if (!isImplementationAvailable())
			return null;
		
		return implementation;
	}	
}
