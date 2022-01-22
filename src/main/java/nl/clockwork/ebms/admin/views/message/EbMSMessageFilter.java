/*
 * Copyright 2021 Ordina
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.clockwork.ebms.admin.views.message;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import nl.clockwork.ebms.EbMSMessageStatus;
import nl.clockwork.ebms.service.model.MessageFilter;
import nl.clockwork.ebms.service.model.Party;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class EbMSMessageFilter extends MessageFilter
{
	private static final long serialVersionUID = 1L;
	final Integer messageNr;
	final Boolean serviceMessage;
	Set<EbMSMessageStatus> statuses;
	LocalDateTime from;
	LocalDateTime to;

	@Builder(builderMethodName = "ebMSMessageFilterBuilder")
	public EbMSMessageFilter(String cpaId, Party fromParty, Party toParty, String service, String action, Instant timestamp, String conversationId, String messageId, String refToMessageId, EbMSMessageStatus messageStatus, Integer messageNr, Boolean serviceMessage, Set<EbMSMessageStatus> statuses, LocalDateTime from, LocalDateTime to)
	{
		super(cpaId,fromParty,toParty,service,action,conversationId,messageId,refToMessageId);
		this.messageNr = messageNr;
		this.serviceMessage = serviceMessage;
		this.statuses = statuses == null ? Collections.emptySet() : statuses;
		this.from = from;
		this.to = to;
	}

	public void reset()
	{
		setCpaId(null);
		setFromParty(null);
		setToParty(null);
		setService(null);
		setAction(null);
		setConversationId(null);
		setMessageId(null);
		setRefToMessageId(null);
		statuses = Collections.emptySet();
		from = null;
		to = null;
	}
}
