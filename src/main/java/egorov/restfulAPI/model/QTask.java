package egorov.restfulAPI.model;

import com.querydsl.core.annotations.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;
import egorov.restfulAPI.Status;

import java.time.ZonedDateTime;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTask extends EntityPathBase<Task> {

    public static final QTask task = new QTask("task");

    public final NumberPath<Long> user = createNumber("user.id", Long.class);

    public final EnumPath<Status> status = createEnum("status", Status.class);

    public final TimePath<ZonedDateTime> startDate = createTime("start_date", ZonedDateTime.class);

    public final TimePath<ZonedDateTime> endDate = createTime("end_date", ZonedDateTime.class);

    public final StringPath name = createString("name");

    public final StringPath description = createString("description");

    public QTask(String variable) {
        super(Task.class, forVariable(variable));
    }

    public QTask(Path<? extends Task> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTask(PathMetadata metadata) {
        super(Task.class, metadata);
    }

}
