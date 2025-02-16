package com.spring.marketplace.database.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 1925137132L;

    public static final QUser user = new QUser("user");

    public final QAuditingEntity _super = new QAuditingEntity(this);

    //inherited
    public final DateTimePath<java.time.Instant> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final SetPath<Favorite, QFavorite> favorites = this.<Favorite, QFavorite>createSet("favorites", Favorite.class, QFavorite.class, PathInits.DIRECT2);

    public final NumberPath<java.math.BigInteger> id = createNumber("id", java.math.BigInteger.class);

    public final SetPath<Message, QMessage> messages = this.<Message, QMessage>createSet("messages", Message.class, QMessage.class, PathInits.DIRECT2);

    public final StringPath password = createString("password");

    public final SetPath<Product, QProduct> products = this.<Product, QProduct>createSet("products", Product.class, QProduct.class, PathInits.DIRECT2);

    public final EnumPath<Role> role = createEnum("role", Role.class);

    public final StringPath username = createString("username");

    public final SetPath<UserChat, QUserChat> usersChats = this.<UserChat, QUserChat>createSet("usersChats", UserChat.class, QUserChat.class, PathInits.DIRECT2);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

