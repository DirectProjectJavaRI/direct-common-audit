package org.nhindirect.common.audit.impl;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.nhindirect.common.audit.AuditContext;
import org.nhindirect.common.audit.AuditEvent;
import org.nhindirect.common.audit.Auditor;
import org.nhindirect.common.audit.DefaultAuditContext;

class MultiProviderAuditorTest 
{
	private static final String PRINCIPAL = "JUNITTEST";
	private static final AuditEvent UNIT_TEST_EVENT = new AuditEvent("name", "value");	
	
	@Test
	public void testCreateAuditor_NullAuditors_AssertException()
	{
		
		Assertions.assertThrows(IllegalArgumentException.class, () ->
		{
			new MultiProviderAuditor(null);
		});
	}
	
	@Test
	public void testCreateAuditor_EmptyAuditors_AssertException()
	{
		
		Assertions.assertThrows(IllegalArgumentException.class, () ->
		{
			new MultiProviderAuditor(new ArrayList<Auditor>());
		});		
	}	
	
	@Test
	public void testAuditEvent()
	{
		Auditor auditor = new MultiProviderAuditor(Arrays.asList(new LoggingAuditor(), new NoOpAuditor()));
		auditor.audit(PRINCIPAL, UNIT_TEST_EVENT);
	}
	
	@Test
	public void testAudit_NullPrincipal_AssertExeption()
	{
		Auditor auditor = new MultiProviderAuditor(Arrays.asList(new LoggingAuditor(), new NoOpAuditor()));
		
		Assertions.assertThrows(IllegalArgumentException.class, () ->
		{
			auditor.audit(null, UNIT_TEST_EVENT);
		});
	}	
	
	@Test
	public void testAuditCategoryAndMessage_EmptyPrincipal_AssertExeption()
	{
		Auditor auditor = new MultiProviderAuditor(Arrays.asList(new LoggingAuditor(), new NoOpAuditor()));
		
		Assertions.assertThrows(IllegalArgumentException.class, () ->
		{
			auditor.audit("", UNIT_TEST_EVENT);
		});
	}	
	
	@Test
	public void testAudit_NullEvent_AssertExeption()
	{
		Auditor auditor = new MultiProviderAuditor(Arrays.asList(new LoggingAuditor(), new NoOpAuditor()));
		
		Assertions.assertThrows(IllegalArgumentException.class, () ->
		{
			auditor.audit(PRINCIPAL, null);
		});
	}		
		
	
	@Test
	public void testAuditWithContext()
	{
		Auditor auditor = new MultiProviderAuditor(Arrays.asList(new LoggingAuditor(), new NoOpAuditor()));
		Collection<? extends AuditContext> ctx = Arrays.asList(new DefaultAuditContext("name", "value"));
		auditor.audit(PRINCIPAL, UNIT_TEST_EVENT, ctx);
	}	
	
	@Test
	public void testAuditCategoryAndMessage_OneAuditorFails()
	{
		Auditor auditor = new MultiProviderAuditor(Arrays.asList(new LoggingAuditor(), new ExceptionAuditor()));
	
		auditor.audit(PRINCIPAL, UNIT_TEST_EVENT);
	}		
}
