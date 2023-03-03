package com.capeelectric.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.util.IOUtils;
import com.capeelectric.model.ResponseFile;
import com.capeelectric.service.FileStorageService;
import com.capeelectric.util.UpdateBuildingCountToFile;

@RestController
@RequestMapping("/api/v1")
public class FileUploadController {
	private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);
	
	@Autowired
	private FileStorageService storageService;
	
	@Autowired
	private UpdateBuildingCountToFile updateBuildingCountToFile;

//	Air Termination file upload
	@PostMapping("/lps/upload/{lpsId}/{componentName}/{index}")
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @PathVariable Integer lpsId,
			@PathVariable String componentName, @PathVariable Integer index)
			throws IOException, SerialException, SQLException {
		logger.debug("File Upload Start");

		storageService.store(file, lpsId, componentName, index);
		logger.debug("File Upload 	End");
		return new ResponseEntity<String>("File  Upload Successfully", HttpStatus.OK);
	}

//	Basic File upload
	@PostMapping("/lps/upload/{fileSize}")
	public ResponseEntity<Integer> basicUploadFile(@RequestParam("file") MultipartFile file, @PathVariable String fileSize)
			throws IOException, SerialException, SQLException {
		logger.debug("File basicUploadFile Start");

		logger.debug("File Upload End");
		return new ResponseEntity<Integer>(storageService.store(file,fileSize), HttpStatus.OK);
	}
	@GetMapping("/lps/downloadFile/{fileId}/{componentName}/{fileName}")
	public ResponseEntity<String> downloadFile(@PathVariable Integer fileId, HttpServletResponse response,
			@PathVariable String componentName, @PathVariable String fileName) throws IOException, SQLException {
		logger.debug("DownloadFile File Start FileId : {}", fileId, componentName);
		ResponseFile fileDB = storageService.downloadFile(fileId, componentName, fileName);
		response.setHeader("Content-Disposition", "inline; fileDB.getfileId()=\"" + fileDB.getFileId() + "\"");
		OutputStream out = response.getOutputStream();
		response.setContentType(fileDB.getFileName());
		IOUtils.copy(fileDB.getData().getBinaryStream(), out);
		out.flush();
		out.close();
		return null;

	}
	
	@GetMapping("/lps/downloadFile/{fileId}")
	public ResponseEntity<String> downloadBasicFile(@PathVariable Integer fileId, HttpServletResponse response) throws IOException, SQLException {
		logger.debug("DownloadFile File Start FileId : {}", fileId);
		ResponseFile fileDB = storageService.downloadFile(fileId);
		response.setHeader("Content-Disposition", "inline; fileDB.getfileId()=\"" + fileDB.getFileId() + "\"");
		OutputStream out = response.getOutputStream();
		response.setContentType(fileDB.getFileName());
		IOUtils.copy(fileDB.getData().getBinaryStream(), out);
		out.flush();
		out.close();
		return null;

	}

	@GetMapping("/lps/retrieveFileName/{lpsId}")
	public ResponseEntity<List<Map<String,String>>> retrieveFileNameByLpsId(@PathVariable Integer lpsId) throws IOException, SQLException {
		logger.debug("Retrieve File Start lpsId : {}", lpsId);
		List<ResponseFile> fileDB = storageService.retrieveFileNameByLpsId(lpsId);

		if (null != fileDB) {
			List<Map<String,String>> list = new ArrayList<Map<String,String>>();
			for (ResponseFile responseFile : fileDB) {
				if (null != responseFile) {
					String buildingCount = "";
					if(responseFile.getBuildingCount() != null) {
						buildingCount = responseFile.getBuildingCount().toString();
					}
					HashMap<String, String> hashMap = new HashMap<>();
					hashMap.put("fileId", responseFile.getFileId().toString());
					hashMap.put("fileType", responseFile.getFileType());
					hashMap.put("fileLpsId", responseFile.getLpsId().toString());
					hashMap.put("fileName", responseFile.getFileName());
					hashMap.put("componentName", responseFile.getComponentName());
					hashMap.put("index", responseFile.getIndex().toString());
					hashMap.put("buildingCount", buildingCount);
					list.add(hashMap);
				}

			}
			return new ResponseEntity<List<Map<String,String>>>(list, HttpStatus.OK);
		}
		return null;

	}

	@GetMapping("/lps/retrieveBasicFile/{fileId}")
	public ResponseEntity<Map<String, String> > retrieveFileName(@PathVariable Integer fileId)
			throws IOException, SQLException {
		logger.debug("Retrieve File Start FileId : {}", fileId);
		ResponseFile basicFile = storageService.retrieveFiled(fileId);
		Map<String, String> hashMap = null ;
		if (null != basicFile) {
			hashMap = new HashMap<String, String>();
			hashMap.put("fileId", basicFile.getFileId().toString());
			hashMap.put("fileType", basicFile.getFileType());
			hashMap.put("fileName", basicFile.getFileName());
			hashMap.put("fileSize", basicFile.getFileSize());
		 
		}
		return new ResponseEntity<Map<String, String> >(hashMap, HttpStatus.OK);

	}

	@PutMapping("/lps/updateFile/{componentName}/{fileId}/{index}")
	public ResponseEntity<String> updateFile(@RequestParam("file") MultipartFile file,@PathVariable String componentName, @PathVariable Integer fileId, @PathVariable Integer index)
			throws IOException, SerialException, SQLException {
		logger.debug("UpdateFile File Start");
		storageService.updateFile(file,componentName, fileId,index);
		logger.debug("UpdateFile File End");
		return new ResponseEntity<String>("File Updated Successfully", HttpStatus.OK);
	}
	
	@PutMapping("/lps/updateBasicFile/{fileId}/{fileSize}")
	public ResponseEntity<String> updateBasicFile(@RequestParam("file") MultipartFile file,@PathVariable Integer fileId,@PathVariable String fileSize)
			throws IOException, SerialException, SQLException {
		logger.debug("updateBasicFile File Start");
		storageService.updateBasicFile(file, fileId,fileSize);
		logger.debug("updateBasicFile File End");
		return new ResponseEntity<String>("File Updated Successfully", HttpStatus.OK);
	}
	
	@PutMapping("/lps/updateFileId/{basicLpsId}/{index}")	
	public void updateFileId(@PathVariable Integer basicLpsId, @PathVariable Integer index)
			throws IOException, SerialException, SQLException {
		logger.debug("UpdateFile File Start");
		storageService.updateFileId(basicLpsId,index);
		logger.debug("UpdateFile File End");
	}
	
	@PutMapping("/updateAllFileId/{basicLpsId}")
	public void updateAllFileId(@RequestBody List<String> listOfResponseFile, @PathVariable Integer basicLpsId)
			throws IOException, SerialException, SQLException {
		logger.debug("UpdateFile File Start");
		storageService.updateAllFileId(listOfResponseFile,basicLpsId);
		logger.debug("UpdateFile File End");
	}

	@DeleteMapping("/lps/removeFile/{basicLpsId}")
	public ResponseEntity<String> removeUnusedFile(@PathVariable Integer basicLpsId) throws IOException {
		logger.debug("UnusedFile Remove Start");
		updateBuildingCountToFile.removeUnusedFiles(basicLpsId);
		logger.debug("UnusedFile Remove End");
		return new ResponseEntity<String>("File  Deleted Successfully", HttpStatus.OK);
	}
}
