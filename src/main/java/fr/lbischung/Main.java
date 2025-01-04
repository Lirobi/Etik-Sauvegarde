package fr.lbischung;

import fr.lbischung.controller.Controller;
import fr.lbischung.model.Model;
import fr.lbischung.view.Display;

public class Main {
    public static void main(String[] args) {
        try {
            Model model = new Model();
            Controller controller = new Controller(model);
            Display display = new Display(controller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}