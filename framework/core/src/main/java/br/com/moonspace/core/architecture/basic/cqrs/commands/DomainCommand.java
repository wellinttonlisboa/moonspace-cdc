package br.com.moonspace.core.architecture.basic.cqrs.commands;

import br.com.moonspace.core.messages.Message;
import lombok.EqualsAndHashCode;

/**
 * Utilized for create a new command agent.
 */
@EqualsAndHashCode(callSuper = true)
public abstract class DomainCommand extends Message {

    /**
     * Constructor with an id.
     *
     * @param id Identifier from class.
     */
    protected DomainCommand(String id) {
        super(id);
    }

}