package org.zerock.mreview.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mreview.dto.MovieDTO;
import org.zerock.mreview.dto.PageRequestDTO;
import org.zerock.mreview.dto.PageResultDTO;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.MovieImage;
import org.zerock.mreview.repository.MovieImageRepository;
import org.zerock.mreview.repository.MovieRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final MovieImageRepository imageRepository;


    @Transactional // 둘 다 성공할때만 커밋하라는 뜻으로 @Transactional을 검
    @Override
    public Long register(MovieDTO movieDTO) {
        Map<String,Object> entityMap=dtoToEntity(movieDTO);
        Movie movie=(Movie) entityMap.get("movie");
        List<MovieImage> movieImageList=(List<MovieImage>)entityMap.get("imgList");
        movieRepository.save(movie); // movie insert
        movieImageList.forEach(movieImage -> {
            imageRepository.save(movieImage); // movie image insert
        });

        return movie.getMno();
    }

    @Override
    public PageResultDTO<MovieDTO, Object[]> getList(PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("mno").descending());
        Page<Object[]> result = movieRepository.getListPage(pageable);

        //함수 레퍼런스 생성. fn타입이 function. fn람다식을 46 line으로 전달. arr[2]는 평점, arr[3]은 리뷰 개수
        Function<Object[], MovieDTO> fn = (arr -> entitiesToDTO((Movie)arr[0],(List<MovieImage>)(Arrays.asList((MovieImage)arr[1])),(Double) arr[2],(Long)arr[3]));

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public MovieDTO getMovie(Long mno) {
        List<Object[]> result=movieRepository.getMovieWithAll(mno);
        Movie movie=(Movie) result.get(0)[0]; // 첫번째꺼 하나만 읽어옴
        List<MovieImage> movieImageList=new ArrayList<>();

        result.forEach(arr -> {
            MovieImage movieImage = (MovieImage)arr[1];
            movieImageList.add(movieImage);
        });

        Double avg = (Double) result.get(0)[2];
        Long reviewCnt = (Long) result.get(0)[3];

        return entitiesToDTO(movie, movieImageList, avg, reviewCnt);
    }
}
