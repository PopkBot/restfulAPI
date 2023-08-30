package egorov.restfulAPI.mapper;

import egorov.restfulAPI.dto.TaskDto;
import egorov.restfulAPI.model.Task;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.convention.NameTokenizers;
import org.modelmapper.convention.NamingConventions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class TaskMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public TaskMapper() {
        this.modelMapper = new ModelMapper();
        Configuration configuration = modelMapper.getConfiguration();
        configuration.setFieldAccessLevel(Configuration.AccessLevel.PUBLIC);
        configuration.setSourceNamingConvention(NamingConventions.JAVABEANS_ACCESSOR);
        configuration.setDestinationNamingConvention(NamingConventions.JAVABEANS_MUTATOR);
        configuration.setSourceNameTokenizer(NameTokenizers.CAMEL_CASE);
        configuration.setDestinationNameTokenizer(NameTokenizers.CAMEL_CASE);
        configuration.setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public Task convertToModel(TaskDto taskDto) {
        Task task = modelMapper.map(taskDto, Task.class);
        task.setStartDate(taskDto.getStartDate().atZone(ZoneId.systemDefault()));
        task.setEndDate(taskDto.getEndDate().atZone(ZoneId.systemDefault()));
        return task;
    }

    public TaskDto convertToDto(Task task) {
        TaskDto taskDto = modelMapper.map(task, TaskDto.class);
        taskDto.setStartDate(task.getStartDate().toLocalDateTime());
        taskDto.setEndDate(task.getEndDate().toLocalDateTime());
        return taskDto;
    }
}
