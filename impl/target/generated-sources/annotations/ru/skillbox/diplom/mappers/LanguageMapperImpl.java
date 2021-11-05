package ru.skillbox.diplom.mappers;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import ru.skillbox.diplom.model.LanguageDataDTO;
import ru.skillbox.diplom.model.api.response.LanguagesResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-09-22T18:26:27+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.11 (Amazon.com Inc.)"
)
public class LanguageMapperImpl implements LanguageMapper {

    @Override
    public LanguagesResponse toLanguageResponse(Integer offset, Integer itemPerPage, List<LanguageDataDTO> dataDTOList) {
        if ( offset == null && itemPerPage == null && dataDTOList == null ) {
            return null;
        }

        LanguagesResponse languagesResponse = new LanguagesResponse();

        if ( offset != null ) {
            languagesResponse.setOffset( offset );
        }
        if ( itemPerPage != null ) {
            languagesResponse.setPerPage( itemPerPage );
        }
        if ( dataDTOList != null ) {
            List<LanguageDataDTO> list = dataDTOList;
            if ( list != null ) {
                languagesResponse.setData( new ArrayList<LanguageDataDTO>( list ) );
            }
        }
        languagesResponse.setTimestamp( new java.util.Date().getTime() );

        return languagesResponse;
    }
}
