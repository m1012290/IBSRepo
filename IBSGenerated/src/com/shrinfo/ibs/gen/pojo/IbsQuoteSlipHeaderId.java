package com.shrinfo.ibs.gen.pojo;

// Generated Mar 25, 2014 2:46:55 AM by Hibernate Tools 3.4.0.CR1



/**
 * IbsQuoteSlipHeaderId generated by hbm2java
 */
public class IbsQuoteSlipHeaderId implements java.io.Serializable {


    private Long id;

    private Long quoteSlipVersion;

    public IbsQuoteSlipHeaderId() {}

    public IbsQuoteSlipHeaderId(Long id, Long quoteSlipVersion) {
        this.id = id;
        this.quoteSlipVersion = quoteSlipVersion;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuoteSlipVersion() {
        return this.quoteSlipVersion;
    }

    public void setQuoteSlipVersion(Long quoteSlipVersion) {
        this.quoteSlipVersion = quoteSlipVersion;
    }


    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof IbsQuoteSlipHeaderId))
            return false;
        IbsQuoteSlipHeaderId castOther = (IbsQuoteSlipHeaderId) other;

        return ((this.getId() == castOther.getId()) || (this.getId() != null
            && castOther.getId() != null && this.getId().equals(castOther.getId())))
            && ((this.getQuoteSlipVersion() == castOther.getQuoteSlipVersion()) || (this
                    .getQuoteSlipVersion() != null && castOther.getQuoteSlipVersion() != null && this
                    .getQuoteSlipVersion().equals(castOther.getQuoteSlipVersion())));
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + (getId() == null ? 0 : this.getId().hashCode());
        result =
            37 * result
                + (getQuoteSlipVersion() == null ? 0 : this.getQuoteSlipVersion().hashCode());
        return result;
    }


}