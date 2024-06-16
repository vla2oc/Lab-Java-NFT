
package com.example.demo.controller;
import com.example.demo.models.AllNft;
import com.example.demo.repo.all_nft_repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class CollectionController {

    @Autowired
    private all_nft_repo all_nft_repo;

    @GetMapping("/")
    public String home(Model model) {
        List<String> images = Arrays.asList(
                "/peng2.jpg",
                "/peng3.jpg",
                "/peng4.jpg"
        );
        model.addAttribute("images", images);
        return "home";
    }

    @GetMapping("/collection")
    public String collection(Model model) {
        Iterable<AllNft> nfts = all_nft_repo.findAll();
        model.addAttribute("nfts", nfts);

        return "collection";
    }

    @GetMapping("/create")
    public String create(Model model) {
        return "create";
    }

    @PostMapping("/create")
    public String nftAdd(@RequestParam("image") MultipartFile image, @RequestParam String title, @RequestParam String creator, @RequestParam String full_text, @RequestParam int price, Model model) {
        String uploadDir = "/src/main/resources/templates/uploads/";
        try {
            Files.createDirectories(Paths.get(uploadDir));
            Path filePath = Paths.get(uploadDir, image.getOriginalFilename());
            image.transferTo(filePath);
            AllNft nft = new AllNft(title, creator, full_text, price);
            nft.setImagePath(filePath.toString());
            all_nft_repo.save(nft);

        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
        return "redirect:/";
    }

    @GetMapping("/collection/{id}")
    public String collectiondetails(@PathVariable(value = "id") long id, Model model) {
        if (!all_nft_repo.existsById(id)) {
            return "redirect:/";
        }
        Optional<AllNft> nft = all_nft_repo.findById(id);
        ArrayList<AllNft> res = new ArrayList<>();
        nft.ifPresent(res::add);
        model.addAttribute("nft", res);
        return "collection-details";
    }

    @GetMapping("/collection/{id}/edit")
    public String collectionedit(@PathVariable(value = "id") long id, Model model) {
        if (!all_nft_repo.existsById(id)) {
            return "redirect:/";
        }
        Optional<AllNft> nft = all_nft_repo.findById(id);
        ArrayList<AllNft> res = new ArrayList<>();
        nft.ifPresent(res::add);
        model.addAttribute("nft", res);
        return "collection-edit";
    }

    @PostMapping("/collection/{id}/edit")
    public String nftupdate(@PathVariable(value = "id") long id, @RequestParam("image") MultipartFile image, @RequestParam String title, @RequestParam String creator, @RequestParam String full_text, @RequestParam int price, Model model) {
        AllNft nft = all_nft_repo.findById(id).orElseThrow();
        nft.setTitle(title);
        nft.setCreator(creator);
        nft.setFull_text(full_text);
        nft.setPrice(price);
        all_nft_repo.save(nft);

        return "redirect:/collection";
    }
    @PostMapping("/collection/{id}/remove")
    public String nftdelete(@PathVariable(value = "id") long id, Model model) {
        AllNft nft = all_nft_repo.findById(id).orElseThrow();
        all_nft_repo.delete(nft);

        return "redirect:/collection";
    }

}


