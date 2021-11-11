package com.example.bank.view;

import com.example.bank.components.CreditOfferEditor;
import com.example.bank.domain.Contribution;
import com.example.bank.domain.CreditOffer;
import com.example.bank.services.ClientService;
import com.example.bank.services.ContributionService;
import com.example.bank.services.CreditOfferService;
import com.example.bank.services.CreditService;
import com.example.bank.services.impl.CreditOfferServiceImpl;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "/offers", layout = MainUi.class)
public class CreditOffersList extends VerticalLayout {

    private final CreditOfferService creditOfferService;
    private final CreditOfferEditor creditOfferEditor;
    private final ContributionService contributionService;
    private final Grid<CreditOffer> creditOfferGrid = new Grid<>(CreditOffer.class);
    private final Grid<Contribution> contributionGrid = new Grid<>(Contribution.class);

    public CreditOffersList(CreditOfferServiceImpl creditOfferService,
                            ClientService clientService,
                            CreditService creditService,
                            ContributionService contributionService) {
        this.creditOfferService = creditOfferService;
        this.contributionService = contributionService;
        Button addNewBtn = new Button("Добавить кредитное предложение");
        add(new Label("Список кредитных предложений:"), addNewBtn);
        creditOfferEditor = new CreditOfferEditor(
                this,
                creditOfferService,
                clientService,
                creditService,
                contributionService);
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        HorizontalLayout layout = new HorizontalLayout(creditOfferGrid, creditOfferEditor);
        layout.setSizeFull();
        contributionGrid.setSizeFull();
        add(layout);
        add(contributionGrid);
        add(creditOfferEditor);
        addNewBtn.addClickListener(e -> creditOfferEditor.setVisible(true));
        creditOfferGrid.asSingleSelect().addValueChangeListener(event ->
                setContributionGrid(creditOfferGrid.asSingleSelect().getValue()));
        updateList();
    }

    public void configureGrid() {
        contributionGrid.addClassName("contribution-grid");
        contributionGrid.setSizeFull();
        contributionGrid.setColumns("payDay", "body", "amount", "pay", "percent");
        creditOfferGrid.addClassName("credit-grid");
        creditOfferGrid.setSizeFull();
        creditOfferGrid.setColumns("amount", "monthCount");
        creditOfferGrid.addColumn(CreditOffer::getClientName).setHeader("Client name");
        creditOfferGrid.addColumn(CreditOffer::getCreditName).setHeader("Credit name");
    }

    public void updateList() {
        creditOfferGrid.setItems(creditOfferService.findAll());
        contributionGrid.setItems(contributionService.findAll());
    }

    public void setContributionGrid(CreditOffer creditOffer) {
        List<Contribution> contributions = contributionService.findAllByCreditOffer(creditOffer); //создаём лист выплат и заносим в него выплаты из БД
        if (contributionService.findAllByCreditOffer(creditOffer).isEmpty()){ //если в бд пусто то
            contributions = contributionService.calculate(creditOffer); //вычисляем график выплат, добавляем их в бд и в текущий лист
        }
        contributionGrid.setItems(contributions);
        creditOfferEditor.set(creditOfferGrid.asSingleSelect().getValue());
    }

}
