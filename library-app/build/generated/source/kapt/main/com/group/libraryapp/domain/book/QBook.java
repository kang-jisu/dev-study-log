package com.group.libraryapp.domain.book;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBook is a Querydsl query type for Book
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBook extends EntityPathBase<Book> {

    private static final long serialVersionUID = -608098416L;

    public static final QBook book = new QBook("book");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final EnumPath<BookType> type = createEnum("type", BookType.class);

    public QBook(String variable) {
        super(Book.class, forVariable(variable));
    }

    public QBook(Path<Book> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBook(PathMetadata metadata) {
        super(Book.class, metadata);
    }

}

