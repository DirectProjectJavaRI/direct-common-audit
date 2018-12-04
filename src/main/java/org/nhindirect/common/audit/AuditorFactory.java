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

package org.nhindirect.common.audit;

import org.nhindirect.common.audit.impl.LoggingAuditor;
import org.nhindirect.common.audit.impl.SPIAuditorFactory;

/**
 * Factory for creating {@link Auditor} instances.  Each call the createAuditor results in a new Auditor instance (the factory does not
 * use a singleton pattern).
 * 
 * @author Greg Meyer
 * @since 6.0
 */
public class AuditorFactory
{
	/**
	 * Creates an {@link Auditor} by searching for configured implementations using Java SPI.  If no implementation are found, a default
	 * instance using the configured logging sub-system is created.
	 * @return An new auditor instance.
	 */
	public static synchronized Auditor createAuditor(ClassLoader cLoader)
	{		
		final SPIAuditorFactory spiProv = SPIAuditorFactory.getInstance(cLoader);
		
		return (spiProv.isImplementationAvailable()) ? spiProv.getAuditorImplementation() : new LoggingAuditor();
	}
}
