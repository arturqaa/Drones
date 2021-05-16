package com.artur.repo.specification;

import com.artur.entity.Account;
import com.artur.entity.UserOrder;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Objects;

public class OrderSpecification implements Specification<UserOrder> {
    private Long userId;


    public OrderSpecification(Long userId) {
        this.userId = userId;
    }

    private Subquery<Account> getUserQuery(final CriteriaQuery<?> query, final CriteriaBuilder builder) {
        var subQuery = query.subquery(Account.class);
        var subRoot = subQuery.from(Account.class);
        return subQuery.select(subRoot).where(builder.equal(subRoot.get("id"), userId));
    }


    @Override
    public Predicate toPredicate(Root<UserOrder> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        var predicate = builder.isTrue(builder.literal(true));

        if (Objects.nonNull(userId)){
            var userQuery = getUserQuery(query, builder);
            predicate = builder.and(predicate, builder.equal(root.join("user"), userQuery));
        }
        return predicate;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
