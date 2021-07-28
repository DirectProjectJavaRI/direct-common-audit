package org.nhindirect.common.audit.impl;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;

import org.nhindirect.common.audit.AuditContext;
import org.nhindirect.common.audit.AuditEvent;
import org.nhindirect.common.audit.DefaultAuditContext;

public class LoggingAuditorTest 
{
	private static final String PRINCIPAL = "JUNITTEST";
	private static final AuditEvent UNIT_TEST_EVENT = new AuditEvent("name", "value");
	
	@Test
	public void testAuditEvent()
	{
		LoggingAuditor auditor = new LoggingAuditor();
		auditor.audit(PRINCIPAL, UNIT_TEST_EVENT);
	}
	
	@Test
	public void testAudit_NullPrincipal_AssertExeption()
	{
		LoggingAuditor auditor = new LoggingAuditor();
		
		Assertions.assertThrows(IllegalArgumentException.class, () ->
		{
			auditor.audit(null, UNIT_TEST_EVENT);
		});
	}	
	
	@Test
	public void testAuditCategoryAndMessage_EmptyPrincipal_AssertExeption()
	{
		LoggingAuditor auditor = new LoggingAuditor();
		
		Assertions.assertThrows(IllegalArgumentException.class, () ->
		{
			auditor.audit("", UNIT_TEST_EVENT);
		});
	}	
	
	@Test
	public void testAudit_NullEvent_AssertExeption()
	{
		LoggingAuditor auditor = new LoggingAuditor();
		
		Assertions.assertThrows(IllegalArgumentException.class, () ->
		{
			auditor.audit(PRINCIPAL, null);
		});
	}		
		
	
	@Test
	public void testAuditWithContext()
	{
		LoggingAuditor auditor = new LoggingAuditor();
		Collection<? extends AuditContext> ctx = Arrays.asList(new DefaultAuditContext("name", "value"));
		auditor.audit(PRINCIPAL, UNIT_TEST_EVENT, ctx);
	}	
}
