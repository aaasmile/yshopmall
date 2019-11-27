package co.yixiang.mp.service.impl;


import co.yixiang.mp.domain.YxArticle;
import co.yixiang.mp.repository.YxArticleRepository;
import co.yixiang.mp.service.YxArticleService;
import co.yixiang.mp.service.dto.YxArticleDTO;
import co.yixiang.mp.service.dto.YxArticleQueryCriteria;
import co.yixiang.mp.service.mapper.YxArticleMapper;
import co.yixiang.utils.OrderUtil;
import co.yixiang.utils.PageUtil;
import co.yixiang.utils.QueryHelp;
import co.yixiang.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
* @author hupeng
* @date 2019-10-07
*/
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxArticleServiceImpl implements YxArticleService {

    @Autowired
    private YxArticleRepository yxArticleRepository;

    @Autowired
    private YxArticleMapper yxArticleMapper;

    @Override
    public Map<String,Object> queryAll(YxArticleQueryCriteria criteria, Pageable pageable){
        Page<YxArticle> page = yxArticleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(yxArticleMapper::toDto));
    }

    @Override
    public List<YxArticleDTO> queryAll(YxArticleQueryCriteria criteria){
        return yxArticleMapper.toDto(yxArticleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public YxArticleDTO findById(Integer id) {
        Optional<YxArticle> yxArticle = yxArticleRepository.findById(id);
        ValidationUtil.isNull(yxArticle,"YxArticle","id",id);
        return yxArticleMapper.toDto(yxArticle.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public YxArticleDTO create(YxArticle resources) {
        resources.setAddTime(String.valueOf(OrderUtil.getSecondTimestampTwo()));
        return yxArticleMapper.toDto(yxArticleRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(YxArticle resources) {
        Optional<YxArticle> optionalYxArticle = yxArticleRepository.findById(resources.getId());
        ValidationUtil.isNull( optionalYxArticle,"YxArticle","id",resources.getId());
        YxArticle yxArticle = optionalYxArticle.get();
        yxArticle.copy(resources);
        yxArticleRepository.save(yxArticle);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        yxArticleRepository.deleteById(id);
    }
}