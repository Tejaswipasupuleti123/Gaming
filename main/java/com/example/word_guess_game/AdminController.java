package com.example.word_guess_game;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
@Controller
@RequestMapping("admin/")
public class AdminController {
    @Autowired
    private WordService wordService;
    private static final String UPLOAD_DIR = "src/main/resources/static/";
    @GetMapping("words")
    public String getAllWords(HttpSession session, Model model) {


        User user = (User) session.getAttribute("user");
        if (user != null && user.getType().equals("admin")) {
            List<Word> words = wordService.getAllWords();
            model.addAttribute("words", words);
            model.addAttribute("user", user);
            return "word-list";
        } else {
            return "redirect:/";
        }
    }
    @GetMapping("add-word")
    public String addWordForm(HttpSession session,Model model) {

        User user = (User) session.getAttribute("user");
        if (user != null && user.getType().equals("admin")) {
            model.addAttribute("word", null);
            model.addAttribute("user", user);// Ensure the form is for adding new word
            return "add-word";

        } else {
            return "redirect:/";
        }
    }
    @PostMapping("add-word")
    public String addWord(@RequestParam String wordName, @RequestParam String hints,@RequestParam String level,
                          @RequestParam MultipartFile imageFile, Model model,HttpSession session) throws IOException {


        User user = (User) session.getAttribute("user");
        if (user != null && user.getType().equals("admin")) {

            String imageName = imageFile.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + imageName);
            Files.createDirectories(path.getParent()); // Ensure directory exists
            Files.write(path, imageFile.getBytes());
            Word word = new Word();
            word.setWordName(wordName);
            word.setHints(hints);
            word.setLevel(level);

            word.setImage(imageName);
            wordService.saveWord(word);
            model.addAttribute("user", user);
            return "redirect:/admin/words";

        } else {
            return "redirect:/";
        }

    }
    @GetMapping("update-word")
    public String updateWordForm(@RequestParam Long id, Model model,HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user != null && user.getType().equals("admin")) {
            Word word = wordService.getWordById(id);
            model.addAttribute("word", word); // Pass the existing word to the form
            model.addAttribute("user", user);
            return "add-word";


        } else {
            return "redirect:/";
        }
    }
    @PostMapping("update-word-done")
    public String updateWord(@RequestParam Long wid, @RequestParam String wordName,
                             @RequestParam String hints, @RequestParam String level, @RequestParam(required = false) MultipartFile imageFile, HttpSession session,Model model)
            throws IOException {

        User user = (User) session.getAttribute("user");
        if (user != null && user.getType().equals("admin")) {

            Word existingWord = wordService.getWordById(wid);
            existingWord.setWordName(wordName);
            existingWord.setHints(hints);
            existingWord.setLevel(level);
            // Check if a new image file is uploaded
            if (imageFile != null && !imageFile.isEmpty()) {
                // Delete the old image
                String oldImageName = existingWord.getImage();
                Path oldImagePath = Paths.get(UPLOAD_DIR + oldImageName);
                if (Files.exists(oldImagePath)) {
                    Files.delete(oldImagePath);
                }
                // Save the new image
                String newImageName = imageFile.getOriginalFilename();
                Path newPath = Paths.get(UPLOAD_DIR + newImageName);
                Files.write(newPath, imageFile.getBytes());
                // Update the word with the new image
                existingWord.setImage(newImageName);
            }
            // Save the updated word
            wordService.saveWord(existingWord);
            model.addAttribute("user", user);
            return "redirect:/admin/words";


        } else {
            return "redirect:/";
        }

    }
    @GetMapping("delete-word")
    public String deleteWord(@RequestParam Long id,HttpSession session, Model model) throws IOException {

        User user = (User) session.getAttribute("user");
        if (user != null && user.getType().equals("admin")) {
            Word word = wordService.getWordById(id);
            // Delete the image file when the word is deleted
            String imageName = word.getImage();
            Path imagePath = Paths.get(UPLOAD_DIR + imageName);
            if (Files.exists(imagePath)) {
                Files.delete(imagePath); // Delete the image file
            }
            wordService.deleteWord(id); // Delete the word from the database
            model.addAttribute("user", user);
            return "redirect:/admin/words";
        } else {
            return "redirect:/";
        }

    }
}
