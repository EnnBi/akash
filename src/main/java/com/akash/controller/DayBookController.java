package com.akash.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.akash.entity.DayBook;
import com.akash.repository.DayBookRepository;

@Controller
@RequestMapping("/day-book")
public class DayBookController {

	@Autowired
	DayBookRepository daybookRepository;
	@GetMapping("/")
	public String add(Model model)
	{
		model.addAttribute("dayBook", new DayBook());
		return null;
	}
	@GetMapping("/save")
	public String save(@ModelAttribute("dayBook") DayBook dayBook,Model model)
	{
		daybookRepository.save(dayBook);
		return null;
	}
	@GetMapping("/update")
	public String update(@PathVariable("id") long id,Model model)
	{
		model.addAttribute("dayBook", daybookRepository.findById(id));
		return null;
	}
	@GetMapping("/delete")
	public String delete(@PathVariable("id") long id)
	{
		daybookRepository.deleteById(id);
		return null;
	}
	@GetMapping("/list")
	public String list(Model model)
	{
		model.addAttribute("list",daybookRepository.findAll());
		
		return null;
	}
}
