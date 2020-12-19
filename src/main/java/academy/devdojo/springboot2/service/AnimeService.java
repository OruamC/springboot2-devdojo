package academy.devdojo.springboot2.service;

import java.util.List;

import academy.devdojo.springboot2.exception.BadRequestException;
import academy.devdojo.springboot2.mapper.AnimeMapper;
import academy.devdojo.springboot2.repository.AnimeRepository;
import academy.devdojo.springboot2.requests.AnimePostRequestBody;
import academy.devdojo.springboot2.requests.AnimePutRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import academy.devdojo.springboot2.domain.Anime;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class AnimeService {

	private final AnimeRepository animeRepository;
	
	public Page<Anime> listAll(Pageable pageable) {
		return animeRepository.findAll(pageable);
	}

	public List<Anime> findByName(String name) {
		return animeRepository.findByName(name);
	}
	
	public Anime findByIdOrThrowBadRequestException(Long id) {
		return	animeRepository.findById(id)
				.orElseThrow(() -> new BadRequestException("Anime not Found"));
	}

	@Transactional
	public Anime save(AnimePostRequestBody animePostRequestBody) {
		return animeRepository.save(AnimeMapper.INSTANCE.toAnime(animePostRequestBody));
	}

	public void delete(Long id) {
		animeRepository.delete(findByIdOrThrowBadRequestException(id));
	}

	public void replace(AnimePutRequestBody animePutRequestBody) {
		Anime savedAnime = findByIdOrThrowBadRequestException(animePutRequestBody.getId());
		Anime anime = AnimeMapper.INSTANCE.toAnime(animePutRequestBody);
		anime.setId(savedAnime.getId());
		animeRepository.save(anime);
	}
}
