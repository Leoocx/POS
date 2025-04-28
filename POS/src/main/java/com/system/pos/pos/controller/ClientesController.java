package com.system.pos.pos.controller;

import com.system.pos.pos.database.ClienteDAO;
import com.system.pos.pos.model.Cliente;
import com.system.pos.pos.model.Endereco;
import com.system.pos.pos.model.TipoCliente;
import com.system.pos.pos.service.ClienteService;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

public class ClientesController {

    public void cadastrarCliente(Cliente cliente) {
            try {
                ClienteService.cadastrarCliente(cliente);
            } catch (Exception ex) {
                System.out.println("Erro ao cadastrar cliente: " + ex.getMessage());
                ex.printStackTrace();
            }
    }
}

