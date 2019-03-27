package com.dyw.client.entity.protection;

import java.util.List;

public class IdentifyEntity {
    private String relationId;//布防的关联关系 ID
    private float maxsimilarity;//所有匹配人员比对结果中的最大相似度
    private List<CandidateEntity> candidate;//匹配的人员名单信息列表, 同一个布防关联对应多个匹配人员

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public float getMaxsimilarity() {
        return maxsimilarity;
    }

    public void setMaxsimilarity(float maxsimilarity) {
        this.maxsimilarity = maxsimilarity;
    }

    public List<CandidateEntity> getCandidate() {
        return candidate;
    }

    public void setCandidate(List<CandidateEntity> candidate) {
        this.candidate = candidate;
    }
}
