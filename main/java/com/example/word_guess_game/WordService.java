package com.example.word_guess_game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class WordService {
    @Autowired
    private WordRepository wordRepository;
    public List<Word> getAllWords() {
        return wordRepository.findAll();
    }
    public Word getWordById(Long id) {
        return wordRepository.findById(id).orElse(null);
    }
    public Word saveWord(Word word) {
        return wordRepository.save(word);
    }
    public void deleteWord(Long id) {
        wordRepository.deleteById(id);
    }
}
