package example.day09.service;

import java.util.List;

import org.springframework.stereotype.Service;

import example.day09.model.dto.ApiDto;
import example.day09.model.entity.ApiEntity;
import example.day09.model.repository.ApiRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
@Transactional 
public class ApiService {
    private final ApiRepository apiRepository;
    public List<ApiDto> findAll(){
        List<ApiEntity> apiEntities = apiRepository.findAll();
        List<ApiDto> apiDtos = apiEntities.stream().map( (entity) -> {return ApiDto.from(entity);} ).toList();
        return apiDtos;
    }

    public boolean save( ApiDto apiDto ){
        ApiEntity apiEntity = apiDto.toEntity(); // 1. dto -> entity
        ApiEntity saveed =  apiRepository.save( apiEntity );// 2. 리포지토리 save 
        if( saveed.getIdx() >=1 ) {// 3. save 결과 판단
            return true;
        }
        return false;
    }


}
