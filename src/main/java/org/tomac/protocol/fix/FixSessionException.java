package org.tomac.protocol.fix;

public class FixSessionException extends Exception {
    private final SessionRejectReason sessionRejectReason;
    private final int tag;
    private final int msgType;
    
    public FixSessionException(String message) {
        super(message);
        this.sessionRejectReason = SessionRejectReason.OTHER;
        this.tag = 0;
        this.msgType = 0;
    }
    
    public FixSessionException(String message, SessionRejectReason sessionRejectReason) {
        super(message);
        this.sessionRejectReason = sessionRejectReason;
        this.tag = 0;
        this.msgType = 0;
    }
    
    public FixSessionException(long rejectReason, String message, int tag, int msgType) {
        super(message);
        this.sessionRejectReason = SessionRejectReason.fromValue(rejectReason);
        this.tag = tag;
        this.msgType = msgType;
    }
    
    public FixSessionException(SessionRejectReason sessionRejectReason, String message, int tag, int msgType) {
        super(message);
        this.sessionRejectReason = sessionRejectReason;
        this.tag = tag;
        this.msgType = msgType;
    }
    
    public SessionRejectReason getSessionRejectReason() {
        return sessionRejectReason;
    }
    
    public int getTag() {
        return tag;
    }
    
    public int getMsgType() {
        return msgType;
    }
}
