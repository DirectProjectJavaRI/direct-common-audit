package org.nhindirect.common.audit.impl;

import org.nhindirect.common.audit.impl.entity.AuditContext;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Flux;

public interface RDBMSAuditContextRepository extends ReactiveCrudRepository<AuditContext, Long>
{
	public Flux<AuditContext> findByAuditEventId(long auditEventId);
}
