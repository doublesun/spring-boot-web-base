package com.ywrain.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActionPointExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ActionPointExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andClientTypeIsNull() {
            addCriterion("clientType is null");
            return (Criteria) this;
        }

        public Criteria andClientTypeIsNotNull() {
            addCriterion("clientType is not null");
            return (Criteria) this;
        }

        public Criteria andClientTypeEqualTo(Integer value) {
            addCriterion("clientType =", value, "clientType");
            return (Criteria) this;
        }

        public Criteria andClientTypeNotEqualTo(Integer value) {
            addCriterion("clientType <>", value, "clientType");
            return (Criteria) this;
        }

        public Criteria andClientTypeGreaterThan(Integer value) {
            addCriterion("clientType >", value, "clientType");
            return (Criteria) this;
        }

        public Criteria andClientTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("clientType >=", value, "clientType");
            return (Criteria) this;
        }

        public Criteria andClientTypeLessThan(Integer value) {
            addCriterion("clientType <", value, "clientType");
            return (Criteria) this;
        }

        public Criteria andClientTypeLessThanOrEqualTo(Integer value) {
            addCriterion("clientType <=", value, "clientType");
            return (Criteria) this;
        }

        public Criteria andClientTypeIn(List<Integer> values) {
            addCriterion("clientType in", values, "clientType");
            return (Criteria) this;
        }

        public Criteria andClientTypeNotIn(List<Integer> values) {
            addCriterion("clientType not in", values, "clientType");
            return (Criteria) this;
        }

        public Criteria andClientTypeBetween(Integer value1, Integer value2) {
            addCriterion("clientType between", value1, value2, "clientType");
            return (Criteria) this;
        }

        public Criteria andClientTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("clientType not between", value1, value2, "clientType");
            return (Criteria) this;
        }

        public Criteria andCverStartIsNull() {
            addCriterion("cverStart is null");
            return (Criteria) this;
        }

        public Criteria andCverStartIsNotNull() {
            addCriterion("cverStart is not null");
            return (Criteria) this;
        }

        public Criteria andCverStartEqualTo(Long value) {
            addCriterion("cverStart =", value, "cverStart");
            return (Criteria) this;
        }

        public Criteria andCverStartNotEqualTo(Long value) {
            addCriterion("cverStart <>", value, "cverStart");
            return (Criteria) this;
        }

        public Criteria andCverStartGreaterThan(Long value) {
            addCriterion("cverStart >", value, "cverStart");
            return (Criteria) this;
        }

        public Criteria andCverStartGreaterThanOrEqualTo(Long value) {
            addCriterion("cverStart >=", value, "cverStart");
            return (Criteria) this;
        }

        public Criteria andCverStartLessThan(Long value) {
            addCriterion("cverStart <", value, "cverStart");
            return (Criteria) this;
        }

        public Criteria andCverStartLessThanOrEqualTo(Long value) {
            addCriterion("cverStart <=", value, "cverStart");
            return (Criteria) this;
        }

        public Criteria andCverStartIn(List<Long> values) {
            addCriterion("cverStart in", values, "cverStart");
            return (Criteria) this;
        }

        public Criteria andCverStartNotIn(List<Long> values) {
            addCriterion("cverStart not in", values, "cverStart");
            return (Criteria) this;
        }

        public Criteria andCverStartBetween(Long value1, Long value2) {
            addCriterion("cverStart between", value1, value2, "cverStart");
            return (Criteria) this;
        }

        public Criteria andCverStartNotBetween(Long value1, Long value2) {
            addCriterion("cverStart not between", value1, value2, "cverStart");
            return (Criteria) this;
        }

        public Criteria andCverEndIsNull() {
            addCriterion("cverEnd is null");
            return (Criteria) this;
        }

        public Criteria andCverEndIsNotNull() {
            addCriterion("cverEnd is not null");
            return (Criteria) this;
        }

        public Criteria andCverEndEqualTo(Long value) {
            addCriterion("cverEnd =", value, "cverEnd");
            return (Criteria) this;
        }

        public Criteria andCverEndNotEqualTo(Long value) {
            addCriterion("cverEnd <>", value, "cverEnd");
            return (Criteria) this;
        }

        public Criteria andCverEndGreaterThan(Long value) {
            addCriterion("cverEnd >", value, "cverEnd");
            return (Criteria) this;
        }

        public Criteria andCverEndGreaterThanOrEqualTo(Long value) {
            addCriterion("cverEnd >=", value, "cverEnd");
            return (Criteria) this;
        }

        public Criteria andCverEndLessThan(Long value) {
            addCriterion("cverEnd <", value, "cverEnd");
            return (Criteria) this;
        }

        public Criteria andCverEndLessThanOrEqualTo(Long value) {
            addCriterion("cverEnd <=", value, "cverEnd");
            return (Criteria) this;
        }

        public Criteria andCverEndIn(List<Long> values) {
            addCriterion("cverEnd in", values, "cverEnd");
            return (Criteria) this;
        }

        public Criteria andCverEndNotIn(List<Long> values) {
            addCriterion("cverEnd not in", values, "cverEnd");
            return (Criteria) this;
        }

        public Criteria andCverEndBetween(Long value1, Long value2) {
            addCriterion("cverEnd between", value1, value2, "cverEnd");
            return (Criteria) this;
        }

        public Criteria andCverEndNotBetween(Long value1, Long value2) {
            addCriterion("cverEnd not between", value1, value2, "cverEnd");
            return (Criteria) this;
        }

        public Criteria andPageCodeIsNull() {
            addCriterion("pageCode is null");
            return (Criteria) this;
        }

        public Criteria andPageCodeIsNotNull() {
            addCriterion("pageCode is not null");
            return (Criteria) this;
        }

        public Criteria andPageCodeEqualTo(String value) {
            addCriterion("pageCode =", value, "pageCode");
            return (Criteria) this;
        }

        public Criteria andPageCodeNotEqualTo(String value) {
            addCriterion("pageCode <>", value, "pageCode");
            return (Criteria) this;
        }

        public Criteria andPageCodeGreaterThan(String value) {
            addCriterion("pageCode >", value, "pageCode");
            return (Criteria) this;
        }

        public Criteria andPageCodeGreaterThanOrEqualTo(String value) {
            addCriterion("pageCode >=", value, "pageCode");
            return (Criteria) this;
        }

        public Criteria andPageCodeLessThan(String value) {
            addCriterion("pageCode <", value, "pageCode");
            return (Criteria) this;
        }

        public Criteria andPageCodeLessThanOrEqualTo(String value) {
            addCriterion("pageCode <=", value, "pageCode");
            return (Criteria) this;
        }

        public Criteria andPageCodeLike(String value) {
            addCriterion("pageCode like", value, "pageCode");
            return (Criteria) this;
        }

        public Criteria andPageCodeNotLike(String value) {
            addCriterion("pageCode not like", value, "pageCode");
            return (Criteria) this;
        }

        public Criteria andPageCodeIn(List<String> values) {
            addCriterion("pageCode in", values, "pageCode");
            return (Criteria) this;
        }

        public Criteria andPageCodeNotIn(List<String> values) {
            addCriterion("pageCode not in", values, "pageCode");
            return (Criteria) this;
        }

        public Criteria andPageCodeBetween(String value1, String value2) {
            addCriterion("pageCode between", value1, value2, "pageCode");
            return (Criteria) this;
        }

        public Criteria andPageCodeNotBetween(String value1, String value2) {
            addCriterion("pageCode not between", value1, value2, "pageCode");
            return (Criteria) this;
        }

        public Criteria andPageIsNull() {
            addCriterion("page is null");
            return (Criteria) this;
        }

        public Criteria andPageIsNotNull() {
            addCriterion("page is not null");
            return (Criteria) this;
        }

        public Criteria andPageEqualTo(String value) {
            addCriterion("page =", value, "page");
            return (Criteria) this;
        }

        public Criteria andPageNotEqualTo(String value) {
            addCriterion("page <>", value, "page");
            return (Criteria) this;
        }

        public Criteria andPageGreaterThan(String value) {
            addCriterion("page >", value, "page");
            return (Criteria) this;
        }

        public Criteria andPageGreaterThanOrEqualTo(String value) {
            addCriterion("page >=", value, "page");
            return (Criteria) this;
        }

        public Criteria andPageLessThan(String value) {
            addCriterion("page <", value, "page");
            return (Criteria) this;
        }

        public Criteria andPageLessThanOrEqualTo(String value) {
            addCriterion("page <=", value, "page");
            return (Criteria) this;
        }

        public Criteria andPageLike(String value) {
            addCriterion("page like", value, "page");
            return (Criteria) this;
        }

        public Criteria andPageNotLike(String value) {
            addCriterion("page not like", value, "page");
            return (Criteria) this;
        }

        public Criteria andPageIn(List<String> values) {
            addCriterion("page in", values, "page");
            return (Criteria) this;
        }

        public Criteria andPageNotIn(List<String> values) {
            addCriterion("page not in", values, "page");
            return (Criteria) this;
        }

        public Criteria andPageBetween(String value1, String value2) {
            addCriterion("page between", value1, value2, "page");
            return (Criteria) this;
        }

        public Criteria andPageNotBetween(String value1, String value2) {
            addCriterion("page not between", value1, value2, "page");
            return (Criteria) this;
        }

        public Criteria andViewIsNull() {
            addCriterion("view is null");
            return (Criteria) this;
        }

        public Criteria andViewIsNotNull() {
            addCriterion("view is not null");
            return (Criteria) this;
        }

        public Criteria andViewEqualTo(String value) {
            addCriterion("view =", value, "view");
            return (Criteria) this;
        }

        public Criteria andViewNotEqualTo(String value) {
            addCriterion("view <>", value, "view");
            return (Criteria) this;
        }

        public Criteria andViewGreaterThan(String value) {
            addCriterion("view >", value, "view");
            return (Criteria) this;
        }

        public Criteria andViewGreaterThanOrEqualTo(String value) {
            addCriterion("view >=", value, "view");
            return (Criteria) this;
        }

        public Criteria andViewLessThan(String value) {
            addCriterion("view <", value, "view");
            return (Criteria) this;
        }

        public Criteria andViewLessThanOrEqualTo(String value) {
            addCriterion("view <=", value, "view");
            return (Criteria) this;
        }

        public Criteria andViewLike(String value) {
            addCriterion("view like", value, "view");
            return (Criteria) this;
        }

        public Criteria andViewNotLike(String value) {
            addCriterion("view not like", value, "view");
            return (Criteria) this;
        }

        public Criteria andViewIn(List<String> values) {
            addCriterion("view in", values, "view");
            return (Criteria) this;
        }

        public Criteria andViewNotIn(List<String> values) {
            addCriterion("view not in", values, "view");
            return (Criteria) this;
        }

        public Criteria andViewBetween(String value1, String value2) {
            addCriterion("view between", value1, value2, "view");
            return (Criteria) this;
        }

        public Criteria andViewNotBetween(String value1, String value2) {
            addCriterion("view not between", value1, value2, "view");
            return (Criteria) this;
        }

        public Criteria andEventIdIsNull() {
            addCriterion("eventId is null");
            return (Criteria) this;
        }

        public Criteria andEventIdIsNotNull() {
            addCriterion("eventId is not null");
            return (Criteria) this;
        }

        public Criteria andEventIdEqualTo(String value) {
            addCriterion("eventId =", value, "eventId");
            return (Criteria) this;
        }

        public Criteria andEventIdNotEqualTo(String value) {
            addCriterion("eventId <>", value, "eventId");
            return (Criteria) this;
        }

        public Criteria andEventIdGreaterThan(String value) {
            addCriterion("eventId >", value, "eventId");
            return (Criteria) this;
        }

        public Criteria andEventIdGreaterThanOrEqualTo(String value) {
            addCriterion("eventId >=", value, "eventId");
            return (Criteria) this;
        }

        public Criteria andEventIdLessThan(String value) {
            addCriterion("eventId <", value, "eventId");
            return (Criteria) this;
        }

        public Criteria andEventIdLessThanOrEqualTo(String value) {
            addCriterion("eventId <=", value, "eventId");
            return (Criteria) this;
        }

        public Criteria andEventIdLike(String value) {
            addCriterion("eventId like", value, "eventId");
            return (Criteria) this;
        }

        public Criteria andEventIdNotLike(String value) {
            addCriterion("eventId not like", value, "eventId");
            return (Criteria) this;
        }

        public Criteria andEventIdIn(List<String> values) {
            addCriterion("eventId in", values, "eventId");
            return (Criteria) this;
        }

        public Criteria andEventIdNotIn(List<String> values) {
            addCriterion("eventId not in", values, "eventId");
            return (Criteria) this;
        }

        public Criteria andEventIdBetween(String value1, String value2) {
            addCriterion("eventId between", value1, value2, "eventId");
            return (Criteria) this;
        }

        public Criteria andEventIdNotBetween(String value1, String value2) {
            addCriterion("eventId not between", value1, value2, "eventId");
            return (Criteria) this;
        }

        public Criteria andEventRemarkIsNull() {
            addCriterion("eventRemark is null");
            return (Criteria) this;
        }

        public Criteria andEventRemarkIsNotNull() {
            addCriterion("eventRemark is not null");
            return (Criteria) this;
        }

        public Criteria andEventRemarkEqualTo(String value) {
            addCriterion("eventRemark =", value, "eventRemark");
            return (Criteria) this;
        }

        public Criteria andEventRemarkNotEqualTo(String value) {
            addCriterion("eventRemark <>", value, "eventRemark");
            return (Criteria) this;
        }

        public Criteria andEventRemarkGreaterThan(String value) {
            addCriterion("eventRemark >", value, "eventRemark");
            return (Criteria) this;
        }

        public Criteria andEventRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("eventRemark >=", value, "eventRemark");
            return (Criteria) this;
        }

        public Criteria andEventRemarkLessThan(String value) {
            addCriterion("eventRemark <", value, "eventRemark");
            return (Criteria) this;
        }

        public Criteria andEventRemarkLessThanOrEqualTo(String value) {
            addCriterion("eventRemark <=", value, "eventRemark");
            return (Criteria) this;
        }

        public Criteria andEventRemarkLike(String value) {
            addCriterion("eventRemark like", value, "eventRemark");
            return (Criteria) this;
        }

        public Criteria andEventRemarkNotLike(String value) {
            addCriterion("eventRemark not like", value, "eventRemark");
            return (Criteria) this;
        }

        public Criteria andEventRemarkIn(List<String> values) {
            addCriterion("eventRemark in", values, "eventRemark");
            return (Criteria) this;
        }

        public Criteria andEventRemarkNotIn(List<String> values) {
            addCriterion("eventRemark not in", values, "eventRemark");
            return (Criteria) this;
        }

        public Criteria andEventRemarkBetween(String value1, String value2) {
            addCriterion("eventRemark between", value1, value2, "eventRemark");
            return (Criteria) this;
        }

        public Criteria andEventRemarkNotBetween(String value1, String value2) {
            addCriterion("eventRemark not between", value1, value2, "eventRemark");
            return (Criteria) this;
        }

        public Criteria andEventTypeIsNull() {
            addCriterion("eventType is null");
            return (Criteria) this;
        }

        public Criteria andEventTypeIsNotNull() {
            addCriterion("eventType is not null");
            return (Criteria) this;
        }

        public Criteria andEventTypeEqualTo(Integer value) {
            addCriterion("eventType =", value, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeNotEqualTo(Integer value) {
            addCriterion("eventType <>", value, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeGreaterThan(Integer value) {
            addCriterion("eventType >", value, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("eventType >=", value, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeLessThan(Integer value) {
            addCriterion("eventType <", value, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeLessThanOrEqualTo(Integer value) {
            addCriterion("eventType <=", value, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeIn(List<Integer> values) {
            addCriterion("eventType in", values, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeNotIn(List<Integer> values) {
            addCriterion("eventType not in", values, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeBetween(Integer value1, Integer value2) {
            addCriterion("eventType between", value1, value2, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("eventType not between", value1, value2, "eventType");
            return (Criteria) this;
        }

        public Criteria andPointTypeIsNull() {
            addCriterion("pointType is null");
            return (Criteria) this;
        }

        public Criteria andPointTypeIsNotNull() {
            addCriterion("pointType is not null");
            return (Criteria) this;
        }

        public Criteria andPointTypeEqualTo(Integer value) {
            addCriterion("pointType =", value, "pointType");
            return (Criteria) this;
        }

        public Criteria andPointTypeNotEqualTo(Integer value) {
            addCriterion("pointType <>", value, "pointType");
            return (Criteria) this;
        }

        public Criteria andPointTypeGreaterThan(Integer value) {
            addCriterion("pointType >", value, "pointType");
            return (Criteria) this;
        }

        public Criteria andPointTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("pointType >=", value, "pointType");
            return (Criteria) this;
        }

        public Criteria andPointTypeLessThan(Integer value) {
            addCriterion("pointType <", value, "pointType");
            return (Criteria) this;
        }

        public Criteria andPointTypeLessThanOrEqualTo(Integer value) {
            addCriterion("pointType <=", value, "pointType");
            return (Criteria) this;
        }

        public Criteria andPointTypeIn(List<Integer> values) {
            addCriterion("pointType in", values, "pointType");
            return (Criteria) this;
        }

        public Criteria andPointTypeNotIn(List<Integer> values) {
            addCriterion("pointType not in", values, "pointType");
            return (Criteria) this;
        }

        public Criteria andPointTypeBetween(Integer value1, Integer value2) {
            addCriterion("pointType between", value1, value2, "pointType");
            return (Criteria) this;
        }

        public Criteria andPointTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("pointType not between", value1, value2, "pointType");
            return (Criteria) this;
        }

        public Criteria andDelIsNull() {
            addCriterion("del is null");
            return (Criteria) this;
        }

        public Criteria andDelIsNotNull() {
            addCriterion("del is not null");
            return (Criteria) this;
        }

        public Criteria andDelEqualTo(Integer value) {
            addCriterion("del =", value, "del");
            return (Criteria) this;
        }

        public Criteria andDelNotEqualTo(Integer value) {
            addCriterion("del <>", value, "del");
            return (Criteria) this;
        }

        public Criteria andDelGreaterThan(Integer value) {
            addCriterion("del >", value, "del");
            return (Criteria) this;
        }

        public Criteria andDelGreaterThanOrEqualTo(Integer value) {
            addCriterion("del >=", value, "del");
            return (Criteria) this;
        }

        public Criteria andDelLessThan(Integer value) {
            addCriterion("del <", value, "del");
            return (Criteria) this;
        }

        public Criteria andDelLessThanOrEqualTo(Integer value) {
            addCriterion("del <=", value, "del");
            return (Criteria) this;
        }

        public Criteria andDelIn(List<Integer> values) {
            addCriterion("del in", values, "del");
            return (Criteria) this;
        }

        public Criteria andDelNotIn(List<Integer> values) {
            addCriterion("del not in", values, "del");
            return (Criteria) this;
        }

        public Criteria andDelBetween(Integer value1, Integer value2) {
            addCriterion("del between", value1, value2, "del");
            return (Criteria) this;
        }

        public Criteria andDelNotBetween(Integer value1, Integer value2) {
            addCriterion("del not between", value1, value2, "del");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("createTime is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("createTime is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("createTime =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("createTime <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("createTime >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("createTime >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("createTime <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("createTime <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("createTime in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("createTime not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("createTime between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("createTime not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("updateTime is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("updateTime is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("updateTime =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("updateTime <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("updateTime >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("updateTime >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("updateTime <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("updateTime <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("updateTime in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("updateTime not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("updateTime between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("updateTime not between", value1, value2, "updateTime");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}