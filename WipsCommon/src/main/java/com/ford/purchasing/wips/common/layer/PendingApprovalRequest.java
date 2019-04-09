package com.ford.purchasing.wips.common.layer;

@SuppressWarnings("javadoc")
public class PendingApprovalRequest extends WipsBaseRequest {

    private Category category;
    private String entityNumber;

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(final Category category) {
        this.category = category;
    }

    /**
     * @return Returns the entityNumber.
     */
    public String getEntityNumber() {
        return this.entityNumber;
    }

    /**
     * @param entityNumber The entityNumber to set.
     */
    public void setEntityNumber(final String entityNumber) {

        this.entityNumber = entityNumber;
    }

}
