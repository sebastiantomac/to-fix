package org.tomac.protocol.fix.messaging;

import java.nio.ByteBuffer;
import org.tomac.protocol.fix.FixSessionException;

public abstract class FixMessage {
    protected ByteBuffer buffer;
    
    public void setBuffer(ByteBuffer buffer) {
        this.buffer = buffer;
    }
    
    public abstract void getAll() throws FixSessionException;
    public abstract void encode(ByteBuffer out);
}
