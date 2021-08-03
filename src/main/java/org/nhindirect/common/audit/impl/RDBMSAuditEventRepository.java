package org.nhindirect.common.audit.impl;

import org.nhindirect.common.audit.impl.entity.AuditEvent;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Flux;

public interface RDBMSAuditEventRepository extends ReactiveCrudRepository<AuditEvent, Long>
{
	@Query("select * from auditEvent ae order by eventTime desc")
	public Flux<AuditEvent> findAllOrderByEventTimeDesc();
	
}
