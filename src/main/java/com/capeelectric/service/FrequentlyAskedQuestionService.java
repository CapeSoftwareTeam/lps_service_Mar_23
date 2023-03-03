package com.capeelectric.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.internal.util.collections.ConcurrentReferenceHashMap.Option;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.capeelectric.exception.FrequentlyAskedQuestionException;
import com.capeelectric.model.FaqSidenav;
import com.capeelectric.model.FrequentlyAskedQuestions;
import com.capeelectric.repository.FrequentlyAskedQuestionRepository;

@Service
public interface FrequentlyAskedQuestionService {

	public static boolean isValidExcelFile(MultipartFile file) {
		return Objects.equals(file.getContentType(),
				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	}

	public static List<FrequentlyAskedQuestions> getQuestionsFromExcel(InputStream inputStream) throws IOException {
		XSSFWorkbook workbook = null;
		List<FrequentlyAskedQuestions> frequentlyAskedQuestion = new ArrayList<>();
		try {
			workbook = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = workbook.getSheetAt(0);
			int rowIndex = 0;
			for (Row row : sheet) {	
				if (rowIndex == 0) {
					rowIndex++;
					continue;
				}
				Iterator<Cell> cellIterator = row.iterator();
				int cellIndex = 0;
				FrequentlyAskedQuestions frequentlyAskedQuestionModel = new FrequentlyAskedQuestions();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					switch (cellIndex) {
					case 0:
						frequentlyAskedQuestionModel.setModuleName(cell.getStringCellValue());
					case 1:
						frequentlyAskedQuestionModel.setCategoryName(cell.getStringCellValue());
					case 2:
						frequentlyAskedQuestionModel.setFaqQuestion(cell.getStringCellValue());
					case 3:
						frequentlyAskedQuestionModel.setFaqAnswer(cell.getStringCellValue());
					case 4:
						frequentlyAskedQuestionModel.setStatus("Active");
					default: {
					}
					}
					cellIndex++;
				}
				frequentlyAskedQuestion.add(frequentlyAskedQuestionModel);
			}

		} catch (IOException e) {

		}
		finally {
			workbook.close();
		}
		return frequentlyAskedQuestion;
	}

	public void addQuestions(List<FrequentlyAskedQuestions> frequentlyAskedQuestion)
			throws FrequentlyAskedQuestionException;

	public FaqSidenav getQuestions() throws FrequentlyAskedQuestionException;

	public void deleteFAQs(Integer questionId) throws FrequentlyAskedQuestionException;

	public void saveCustomersToDatabase(MultipartFile file) throws FrequentlyAskedQuestionException;

//	public void addFaqQuestions(List<FrequentlyAskedQuestions> frequentlyAskedQuestions)
//			throws FrequentlyAskedQuestionException;

	public Boolean isQuestionActive(FrequentlyAskedQuestions frequentlyAskedQuestions)
			throws FrequentlyAskedQuestionException;

	public void saveQuestions(List<FrequentlyAskedQuestions> frequentluAskedQuestions)
			throws FrequentlyAskedQuestionException;

	public List<FrequentlyAskedQuestions> retrieveFaqQuestions() throws FrequentlyAskedQuestionException;

	public void updateFaq(List<FrequentlyAskedQuestions> frequentlyAskedQuestions);

}
