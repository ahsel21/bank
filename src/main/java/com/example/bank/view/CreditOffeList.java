package com.example.bank.view;

import com.example.bank.components.CreditOfferEditor;
import com.example.bank.domain.Contribution;
import com.example.bank.domain.CreditOffer;
import com.example.bank.services.impl.ClientServiceImpl;
import com.example.bank.services.impl.ContributionServiceImpl;
import com.example.bank.services.impl.CreditOfferServiceImpl;
import com.example.bank.services.impl.CreditServiceImpl;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "/offers", layout = MainUi.class)
public class CreditOffeList extends VerticalLayout {

    private CreditOfferServiceImpl creditOfferServiceImpl;
    private CreditOfferEditor creditOfferEditor;
    private ContributionServiceImpl contributionService;
    private final Button addNewBtn = new Button("Добавить кредитное предложение");
    private Grid<CreditOffer> creditOfferGrid = new Grid<>(CreditOffer.class);
    private Grid<Contribution> contributionGrid = new Grid<>(Contribution.class);

    public CreditOffeList(CreditOfferServiceImpl creditOfferServiceImpl,
                          ClientServiceImpl clientService,
                          CreditServiceImpl creditService,
                          ContributionServiceImpl contributionService) {
        this.creditOfferServiceImpl = creditOfferServiceImpl;
        this.contributionService = contributionService;
        add(new Label("Список кредитных предложений:"), addNewBtn);
        creditOfferEditor = new CreditOfferEditor(this, creditOfferServiceImpl, contributionService, clientService, creditService);
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

    private void configureGrid() {
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
        creditOfferGrid.setItems(creditOfferServiceImpl.findAll());
        contributionGrid.setItems(contributionService.findAll());
    }
    public void setContributionGrid(CreditOffer creditOffer) {
        List<Contribution> contributions = contributionService.calculate(creditOffer);
        contributionGrid.setItems(contributions);
        creditOfferEditor.set(creditOfferGrid.asSingleSelect().getValue());
        updateList();
    }
}
