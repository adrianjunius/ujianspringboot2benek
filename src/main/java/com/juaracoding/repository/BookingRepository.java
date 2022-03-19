package com.juaracoding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.juaracoding.model.BookingModel;

public interface BookingRepository extends JpaRepository<BookingModel, Long>{
//	@Query(value = "SELECT `keberangkatan`.`id`, `bus`.`nama_perusahaan` as perusahaan , `kelas`, `harga`,"
//			+ " `tanggal` as waktu, `jurusan`.`deskripsi` FROM keberangkatan"
//			+ " INNER JOIN `bus` ON `bus`.`no_polisi` = `keberangkatan`.`no_polisi` "
//			+ "INNER JOIN `jurusan` ON `jurusan`.`id` = `keberangkatan`.`id_jurusan` WHERE `jurusan`."
//			+ "`terminal_awal` = 'poris' or `tanggal` = '2020-01-01';", nativeQuery = true)
	
	
}
