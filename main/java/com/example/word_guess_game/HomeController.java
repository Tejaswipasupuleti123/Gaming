package com.example.word_guess_game;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    private final UserRepository userRepository;


    public HomeController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }





    @GetMapping("/")
    private String home (){

        return "home";

    }

    @GetMapping("/signup")
    public String addUserForm(Model model) {
       // model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup-v")
    public String saveUser(@ModelAttribute("user") User user, @RequestParam("vpassword") String vpassword, Model model,HttpSession session) {


        // Step 1: Check if any field is empty
        if (user.getName().isEmpty() || user.getEmail().isEmpty() || user.getPassword().isEmpty() || vpassword.isEmpty()) {
            model.addAttribute("error", "All fields are required");
            return "signup"; // Return the signup page with an error message
        }

        // Step 2: Validate email format (simple regex)
        if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            model.addAttribute("error", "Invalid email format");
            return "signup";
        }

        // Step 3: Validate password length
        if (user.getPassword().length() < 6) {
            model.addAttribute("error", "Password must be at least 6 characters long");
            return "signup";
        }

        // Step 4: Check if password and verify password match
        if (!user.getPassword().equals(vpassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "signup";
        }

        // Step 5: Save user if validation passes
        userService.saveUser(user);
        //return "redirect:/login";
        //model.addAttribute("success", "User Created Successfully");
        session.setAttribute("message", "User Created Successfully");
        //return "signup";
        return "redirect:/login";
    }




    @GetMapping("/login")
    public String index() {
        return "login";
    }
    @PostMapping("/login-v")
    public String login(@RequestParam String email, @RequestParam String
            password, HttpSession session, Model model) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("user", user);

            if(user.getType().equals("admin")){
                return "redirect:/admin/words";
            }else if(user.getType().equals("user")){
                return "redirect:/dashboard";
            }
          //  model.addAttribute("error", user.getType());
            return "login";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/";
    }

    @GetMapping("/Score/{userId}/{newScore}")
    //@ResponseBody()
    public String updateScore(@PathVariable Long userId, @PathVariable int
            newScore,HttpSession session,Model model) {

        //return "Score updated successfully";


        User user = (User) session.getAttribute("user");
        user.setScore((long) newScore);
        if (user != null) {
            userService.updateScore(userId, newScore);
            session.setAttribute("win_message", "Congratulations You win");
            model.addAttribute("user", user);
            session.setAttribute("user",user);
            return "redirect:/dashboard";

        } else {
            return "redirect:/";
        }
    }


}
