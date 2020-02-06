package com.xz.blog.modal.po;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StatisticExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public StatisticExample() {
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

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andArticleIdIsNull() {
            addCriterion("aid is null");
            return (Criteria) this;
        }

        public Criteria andArticleIdIsNotNull() {
            addCriterion("aid is not null");
            return (Criteria) this;
        }

        public Criteria andArticleIdEqualTo(Integer value) {
            addCriterion("aid =", value, "articleId");
            return (Criteria) this;
        }

        public Criteria andArticleIdNotEqualTo(Integer value) {
            addCriterion("aid <>", value, "articleId");
            return (Criteria) this;
        }

        public Criteria andArticleIdGreaterThan(Integer value) {
            addCriterion("aid >", value, "articleId");
            return (Criteria) this;
        }

        public Criteria andArticleIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("aid >=", value, "articleId");
            return (Criteria) this;
        }

        public Criteria andArticleIdLessThan(Integer value) {
            addCriterion("aid <", value, "articleId");
            return (Criteria) this;
        }

        public Criteria andArticleIdLessThanOrEqualTo(Integer value) {
            addCriterion("aid <=", value, "articleId");
            return (Criteria) this;
        }

        public Criteria andArticleIdIn(List<Integer> values) {
            addCriterion("aid in", values, "articleId");
            return (Criteria) this;
        }

        public Criteria andArticleIdNotIn(List<Integer> values) {
            addCriterion("aid not in", values, "articleId");
            return (Criteria) this;
        }

        public Criteria andArticleIdBetween(Integer value1, Integer value2) {
            addCriterion("aid between", value1, value2, "articleId");
            return (Criteria) this;
        }

        public Criteria andArticleIdNotBetween(Integer value1, Integer value2) {
            addCriterion("aid not between", value1, value2, "articleId");
            return (Criteria) this;
        }

        public Criteria andCommentCountIsNull() {
            addCriterion("com_c is null");
            return (Criteria) this;
        }

        public Criteria andCommentCountIsNotNull() {
            addCriterion("com_c is not null");
            return (Criteria) this;
        }

        public Criteria andCommentCountEqualTo(Integer value) {
            addCriterion("com_c =", value, "commentCount");
            return (Criteria) this;
        }

        public Criteria andCommentCountNotEqualTo(Integer value) {
            addCriterion("com_c <>", value, "commentCount");
            return (Criteria) this;
        }

        public Criteria andCommentCountGreaterThan(Integer value) {
            addCriterion("com_c >", value, "commentCount");
            return (Criteria) this;
        }

        public Criteria andCommentCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("com_c >=", value, "commentCount");
            return (Criteria) this;
        }

        public Criteria andCommentCountLessThan(Integer value) {
            addCriterion("com_c <", value, "commentCount");
            return (Criteria) this;
        }

        public Criteria andCommentCountLessThanOrEqualTo(Integer value) {
            addCriterion("com_c <=", value, "commentCount");
            return (Criteria) this;
        }

        public Criteria andCommentCountIn(List<Integer> values) {
            addCriterion("com_c in", values, "commentCount");
            return (Criteria) this;
        }

        public Criteria andCommentCountNotIn(List<Integer> values) {
            addCriterion("com_c not in", values, "commentCount");
            return (Criteria) this;
        }

        public Criteria andCommentCountBetween(Integer value1, Integer value2) {
            addCriterion("com_c between", value1, value2, "commentCount");
            return (Criteria) this;
        }

        public Criteria andCommentCountNotBetween(Integer value1, Integer value2) {
            addCriterion("com_c not between", value1, value2, "commentCount");
            return (Criteria) this;
        }

        public Criteria andViewCountIsNull() {
            addCriterion("view_c is null");
            return (Criteria) this;
        }

        public Criteria andViewCountIsNotNull() {
            addCriterion("view_c is not null");
            return (Criteria) this;
        }

        public Criteria andViewCountEqualTo(Integer value) {
            addCriterion("view_c =", value, "viewCount");
            return (Criteria) this;
        }

        public Criteria andViewCountNotEqualTo(Integer value) {
            addCriterion("view_c <>", value, "viewCount");
            return (Criteria) this;
        }

        public Criteria andViewCountGreaterThan(Integer value) {
            addCriterion("view_c >", value, "viewCount");
            return (Criteria) this;
        }

        public Criteria andViewCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("view_c >=", value, "viewCount");
            return (Criteria) this;
        }

        public Criteria andViewCountLessThan(Integer value) {
            addCriterion("view_c <", value, "viewCount");
            return (Criteria) this;
        }

        public Criteria andViewCountLessThanOrEqualTo(Integer value) {
            addCriterion("view_c <=", value, "viewCount");
            return (Criteria) this;
        }

        public Criteria andViewCountIn(List<Integer> values) {
            addCriterion("view_c in", values, "viewCount");
            return (Criteria) this;
        }

        public Criteria andViewCountNotIn(List<Integer> values) {
            addCriterion("view_c not in", values, "viewCount");
            return (Criteria) this;
        }

        public Criteria andViewCountBetween(Integer value1, Integer value2) {
            addCriterion("view_c between", value1, value2, "viewCount");
            return (Criteria) this;
        }

        public Criteria andViewCountNotBetween(Integer value1, Integer value2) {
            addCriterion("view_c not between", value1, value2, "viewCount");
            return (Criteria) this;
        }

        public Criteria andCollectionCountIsNull() {
            addCriterion("col_c is null");
            return (Criteria) this;
        }

        public Criteria andCollectionCountIsNotNull() {
            addCriterion("col_c is not null");
            return (Criteria) this;
        }

        public Criteria andCollectionCountEqualTo(Integer value) {
            addCriterion("col_c =", value, "CollectionCount");
            return (Criteria) this;
        }

        public Criteria andCollectionCountNotEqualTo(Integer value) {
            addCriterion("col_c <>", value, "CollectionCount");
            return (Criteria) this;
        }

        public Criteria andCollectionCountGreaterThan(Integer value) {
            addCriterion("col_c >", value, "CollectionCount");
            return (Criteria) this;
        }

        public Criteria andCollectionCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("col_c >=", value, "CollectionCount");
            return (Criteria) this;
        }

        public Criteria andCollectionCountLessThan(Integer value) {
            addCriterion("col_c <", value, "CollectionCount");
            return (Criteria) this;
        }

        public Criteria andCollectionCountLessThanOrEqualTo(Integer value) {
            addCriterion("col_c <=", value, "CollectionCount");
            return (Criteria) this;
        }

        public Criteria andCollectionCountIn(List<Integer> values) {
            addCriterion("col_c in", values, "CollectionCount");
            return (Criteria) this;
        }

        public Criteria andCollectionCountNotIn(List<Integer> values) {
            addCriterion("col_c not in", values, "CollectionCount");
            return (Criteria) this;
        }

        public Criteria andCollectionCountBetween(Integer value1, Integer value2) {
            addCriterion("col_c between", value1, value2, "CollectionCount");
            return (Criteria) this;
        }

        public Criteria andCollectionCountNotBetween(Integer value1, Integer value2) {
            addCriterion("col_c not between", value1, value2, "CollectionCount");
            return (Criteria) this;
        }

        public Criteria andThumbupCountIsNull() {
            addCriterion("thumbup_c is null");
            return (Criteria) this;
        }

        public Criteria andThumbupCountIsNotNull() {
            addCriterion("thumbup_c is not null");
            return (Criteria) this;
        }

        public Criteria andThumbupCountEqualTo(Integer value) {
            addCriterion("thumbup_c =", value, "ThumbupCount");
            return (Criteria) this;
        }

        public Criteria andThumbupCountNotEqualTo(Integer value) {
            addCriterion("thumbup_c <>", value, "ThumbupCount");
            return (Criteria) this;
        }

        public Criteria andThumbupCountGreaterThan(Integer value) {
            addCriterion("thumbup_c >", value, "ThumbupCount");
            return (Criteria) this;
        }

        public Criteria andThumbupCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("thumbup_c >=", value, "ThumbupCount");
            return (Criteria) this;
        }

        public Criteria andThumbupCountLessThan(Integer value) {
            addCriterion("thumbup_c <", value, "ThumbupCount");
            return (Criteria) this;
        }

        public Criteria andThumbupCountLessThanOrEqualTo(Integer value) {
            addCriterion("thumbup_c <=", value, "ThumbupCount");
            return (Criteria) this;
        }

        public Criteria andThumbupCountIn(List<Integer> values) {
            addCriterion("thumbup_c in", values, "ThumbupCount");
            return (Criteria) this;
        }

        public Criteria andThumbupCountNotIn(List<Integer> values) {
            addCriterion("thumbup_c not in", values, "ThumbupCount");
            return (Criteria) this;
        }

        public Criteria andThumbupCountBetween(Integer value1, Integer value2) {
            addCriterion("thumbup_c between", value1, value2, "ThumbupCount");
            return (Criteria) this;
        }

        public Criteria andThumbupCountNotBetween(Integer value1, Integer value2) {
            addCriterion("thumbup_c not between", value1, value2, "ThumbupCount");
            return (Criteria) this;
        }

        public Criteria andWordCountIsNull() {
            addCriterion("word_c is null");
            return (Criteria) this;
        }

        public Criteria andWordCountIsNotNull() {
            addCriterion("word_c is not null");
            return (Criteria) this;
        }

        public Criteria andWordCountEqualTo(Integer value) {
            addCriterion("word_c =", value, "WordCount");
            return (Criteria) this;
        }

        public Criteria andWordCountNotEqualTo(Integer value) {
            addCriterion("word_c <>", value, "WordCount");
            return (Criteria) this;
        }

        public Criteria andWordCountGreaterThan(Integer value) {
            addCriterion("word_c >", value, "WordCount");
            return (Criteria) this;
        }

        public Criteria andWordCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("word_c >=", value, "WordCount");
            return (Criteria) this;
        }

        public Criteria andWordCountLessThan(Integer value) {
            addCriterion("word_c <", value, "WordCount");
            return (Criteria) this;
        }

        public Criteria andWordCountLessThanOrEqualTo(Integer value) {
            addCriterion("word_c <=", value, "WordCount");
            return (Criteria) this;
        }

        public Criteria andWordCountIn(List<Integer> values) {
            addCriterion("word_c in", values, "WordCount");
            return (Criteria) this;
        }

        public Criteria andWordCountNotIn(List<Integer> values) {
            addCriterion("word_c not in", values, "WordCount");
            return (Criteria) this;
        }

        public Criteria andWordCountBetween(Integer value1, Integer value2) {
            addCriterion("word_c between", value1, value2, "WordCount");
            return (Criteria) this;
        }

        public Criteria andWordCountNotBetween(Integer value1, Integer value2) {
            addCriterion("word_c not between", value1, value2, "WordCount");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
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