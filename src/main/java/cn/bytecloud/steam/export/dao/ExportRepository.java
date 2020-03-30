package cn.bytecloud.steam.export.dao;

import cn.bytecloud.steam.export.entity.Export;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExportRepository extends MongoRepository<Export, String> {
}
