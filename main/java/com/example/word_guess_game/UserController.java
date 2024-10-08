package com.example.word_guess_game;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private WordRepository wordRepository;

    @GetMapping("/dashboard")
    public String showForm(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        String winMessage = (String) session.getAttribute("win_message");
        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("levels", new String[]{"Easy", "Medium", "Hard"});
            model.addAttribute("selectedLevel", "");
            model.addAttribute("message", winMessage);
            return "word-form";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/word")
    public String getWord(@ModelAttribute("selectedLevel") String selectedLevel, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        session.setAttribute("win_message", null);
        if (user != null) {
            Word word = wordRepository.findRandomWordByLevel(selectedLevel);
            model.addAttribute("word", word);
            session.setAttribute("word", word);
            model.addAttribute("user", user);
            return "redirect:/showWord";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/showWord")
    public String showWord(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            Word word = (Word) session.getAttribute("word");
            model.addAttribute("GivenHints", word.getHints());
            model.addAttribute("GivenImage", word.getImage());
            model.addAttribute("user", user);
            return "word-input";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/getWord")
    public String processWord(@RequestParam String word, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            Word currentWord = (Word) session.getAttribute("word");

            if (word != null && currentWord.getWordName().equalsIgnoreCase(word)) {
                model.addAttribute("message", "Congratulations, You win!");
                model.addAttribute("user", user);
                Integer newScore = Math.toIntExact(user.getScore() + 10);
                return "redirect:/Score/" + user.getId() + "/" + newScore;
            } else {
                String selectedLevel = currentWord.getLevel();
                Word newWord = wordRepository.findRandomWordByLevel(selectedLevel);
                session.setAttribute("word", newWord);

                model.addAttribute("message", "Sorry, that's incorrect! Try again with a new word.");
                model.addAttribute("GivenHints", newWord.getHints());
                model.addAttribute("GivenImage", newWord.getImage());
                model.addAttribute("user", user);
                return "word-input";
            }
        } else {
            return "redirect:/";
        }
    }
}