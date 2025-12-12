package ru.p4ejlov0d.dto;

/**
 * <p>Объект, содержащий данные, приходящие от других модулей</p>
 *
 * @param to      адресаты
 * @param subject тема письма
 * @param type    тип письма
 * @author p4Jlov0d
 */
public record NotficationDto(String[] to, String subject, Type type) {
}