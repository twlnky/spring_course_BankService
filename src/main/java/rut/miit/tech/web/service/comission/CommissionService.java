package rut.miit.tech.web.service.comission;

import rut.miit.tech.web.domain.model.Client;

public interface CommissionService {
    float defineCommission(Client client, Long cardId);
}
